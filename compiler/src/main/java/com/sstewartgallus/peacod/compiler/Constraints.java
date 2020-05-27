/*
 * Copyright 2019 Steven Stewart-Gallus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sstewartgallus.peacod.compiler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Constraints {
    // TODO: Just copy paste from here http://dev.stephendiehl.com/fun/006_hindley_milner.html ?
    static Map<Type, Type> solve(Set<Constraint> constraints) {
        Map<Type, Type> solutionsSoFar = new HashMap<>(holes(constraints));
        List<Constraint> unresolvedConstraints = new ArrayList<>(constraints);
        while (unresolvedConstraints.size() > 0) {
            var constraint = unresolvedConstraints.remove(0);
            if (constraint.tag() == Constraint.Tag.EQUALITY) {
                var eq = (Constraint.Equality) constraint;
                var left = eq.left;
                var right = eq.right;

                var xCanonical = resolve(solutionsSoFar, left);
                var yCanonical = resolve(solutionsSoFar, right);

                if (Objects.equals(xCanonical, yCanonical)) {
                    // no new information
                    continue;
                }

                mapHoleBlock:
                {
                    Type key;
                    Type value;
                    if (xCanonical instanceof Type.Hole) {
                        key = xCanonical;
                        value = yCanonical;
                    } else if (yCanonical instanceof Type.Hole) {
                        key = yCanonical;
                        value = xCanonical;
                    } else {
                        break mapHoleBlock;
                    }

                    var m = Collections.singletonMap(key, value);
                    solutionsSoFar.replaceAll((h, b) -> resolve(m, b));
                    continue;
                }

                block:
                {
                    if (!(xCanonical instanceof Type.Concrete)) {
                        break block;
                    }
                    var xConc = (Type.Concrete) xCanonical;

                    if (!(yCanonical instanceof Type.Concrete)) {
                        break block;
                    }
                    var yConc = (Type.Concrete) yCanonical;

                    if (xConc.tag != yConc.tag) {
                        break block;
                    }

                    var xTypes = xConc.types;
                    var yTypes = yConc.types;
                    for (var ii = 0; ii < xTypes.length; ++ii) {
                        unresolvedConstraints.add(
                                Constraint.ofEqual(xTypes[ii], yTypes[ii]));
                    }
                    continue;
                }

                throw new Error("failed unifying (" + xCanonical + ") and (" + yCanonical + ")");
            }
            throw new Error("unhandled constraint");
        }
        return solutionsSoFar;
    }

    // fixme erroneous needs recursion
    private static Map<Type, Type> holes(Set<Constraint> constraints) {
        return constraints
                .stream()
                .flatMap((constraint) -> {
                    if (constraint.tag() == Constraint.Tag.EQUALITY) {
                        var eq = (Constraint.Equality) constraint;
                        var x = eq.left;
                        var y = eq.right;

                        return Stream.of(x, y);
                    }
                    throw new Error("unimpl");
                })
                .distinct()
                .flatMap(Constraints::holes)
                .distinct()
                .collect(Collectors.toMap((t) -> t, (t) -> t));
    }

    private static Stream<Type.Hole> holes(Type type) {
        if (type instanceof Type.Hole) {
            return Stream.of((Type.Hole) type);
        }

        var concrete = (Type.Concrete) type;
        return Arrays
                .stream(concrete.types)
                .flatMap(Constraints::holes)
                .distinct();
    }

    static Type termType(Term term, Set<Constraint> constraints) {
        switch (term.tag()) {
            case REFERENCE: {
                var get = (Term.TermReference) term;
                var scheme = get.scheme.copy();

                for (var ii = 0; ii < get.typeArguments.size(); ++ii) {
                    constraints.add(Constraint.ofEqual(scheme.holes.get(ii), get.typeArguments.get(ii)));
                }
                return scheme.type;
            }

            case APPLY: {
                var apply = (Term.Apply) term;

                var function = apply.function;
                var arguments = apply.arguments;

                var functionType = termType(function, constraints);

                List<Type> argumentTypes = new ArrayList<>();
                for (var argTerm : arguments) {
                    var shouldBe = termType(argTerm, constraints);
                    argumentTypes.add(shouldBe);
                }

                Type result = new Type.Hole();
                var inferred = BuiltinTerms.fn(argumentTypes, result);
                constraints.add(Constraint.ofEqual(functionType, inferred));
                return result;
            }

            case LOAD_ARG: {
                var loadArgument = (Term.Variable) term;
                return loadArgument.type;
            }

            case LIT_BOOLEAN:
                return BuiltinTerms.BOOL_VALUE;

            case LIT_INT:
                return BuiltinTerms.INT_VALUE;

            case LIT_LONG:
                throw new Error("unimpl");

            default:
                throw new Error("unimpl " + term);
        }
    }

    static Term decorate(Map<Type, Type> solutions, Term term) {
        if (term instanceof Term.Literal) {
            return term;
        }
        switch (term.tag()) {
            case REFERENCE: {
                var get = (Term.TermReference) term;
                var typeArguments = get.typeArguments
                        .stream()
                        .map((e) -> resolve(solutions, e))
                        .collect(Collectors.toList());
                var type = resolve(solutions, get.scheme);
                return new Term.TermReference(get.reference, type, typeArguments);
            }

            case APPLY: {
                var apply = (Term.Apply) term;
                var function = decorate(solutions, apply.function);
                var arguments = apply
                        .arguments
                        .stream()
                        .map(e -> decorate(solutions, e))
                        .collect(Collectors.toList());
                return new Term.Apply(function, arguments);
            }

            case LOAD_ARG: {
                var loadArgument = (Term.Variable) term;
                var type = resolve(solutions, loadArgument.type);
                return new Term.Variable(loadArgument.argIndex, type);
            }

            default:
                throw new Error("unimpl " + term);
        }
    }

    static Type resolve(Map<Type, Type> solutions, Type type) {
        if (type instanceof Type.Hole) {
            return solutions.getOrDefault(type, type);
        }

        var concrete = (Type.Concrete) type;
        if (concrete.tag.arity == 0) {
            return type;
        }

        var replaced = Arrays.stream(concrete.types)
                .map((e) -> resolve(solutions, e))
                .toArray(Type[]::new);
        return new Type.Concrete(concrete.tag, replaced);
    }

    private static TypeScheme resolve(Map<Type, Type> solutions, TypeScheme scheme) {
        return TypeScheme.over(scheme.holes, resolve(solutions, scheme.type));
    }
}
