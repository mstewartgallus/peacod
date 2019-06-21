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
            Constraint constraint = unresolvedConstraints.remove(0);
            if (constraint.tag() == Constraint.Tag.EQUALITY) {
                Constraint.Equality eq = (Constraint.Equality) constraint;
                Type left = eq.left;
                Type right = eq.right;

                Type xCanonical = resolve(solutionsSoFar, left);
                Type yCanonical = resolve(solutionsSoFar, right);

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

                    Map<Type, Type> m = Collections.singletonMap(key, value);
                    solutionsSoFar.replaceAll((h, b) -> resolve(m, b));
                    continue;
                }

                block:
                {
                    if (!(xCanonical instanceof Type.Concrete)) {
                        break block;
                    }
                    Type.Concrete xConc = (Type.Concrete) xCanonical;

                    if (!(yCanonical instanceof Type.Concrete)) {
                        break block;
                    }
                    Type.Concrete yConc = (Type.Concrete) yCanonical;

                    if (xConc.tag != yConc.tag) {
                        break block;
                    }

                    Type[] xTypes = xConc.types;
                    Type[] yTypes = yConc.types;
                    for (int ii = 0; ii < xTypes.length; ++ii) {
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
                        Constraint.Equality eq = (Constraint.Equality) constraint;
                        Type x = eq.left;
                        Type y = eq.right;

                        return Stream.of(x, y);
                    }
                    throw new Error("unimpl");
                })
                .distinct()
                .flatMap((type) -> {
                    if (type instanceof Type.Hole) {
                        return Stream.of(type);
                    }

                    Type.Concrete concrete = (Type.Concrete) type;
                    return Arrays.stream(concrete.types);
                })
                .distinct()
                .collect(Collectors.toMap((t) -> t, (t) -> t));
    }

    static Type termType(Term term, Set<Constraint> constraints) {
        switch (term.tag()) {
            case GET: {
                Term.Get get = (Term.Get) term;
                return get.type;
            }

            case APPLY: {
                Term.Apply apply = (Term.Apply) term;

                Term function = apply.function;
                List<Term> arguments = apply.arguments;

                Type functionType = termType(function, constraints);

                List<Type> argumentTypes = new ArrayList<>();
                for (Term argTerm : arguments) {
                    Type shouldBe = termType(argTerm, constraints);
                    argumentTypes.add(shouldBe);
                }

                Type resultType = new Type.Hole();
                argumentTypes.add(resultType);
                Type inferredType = BuiltinTerms.fn(argumentTypes.toArray(new Type[0]));
                constraints.add(Constraint.ofEqual(functionType, inferredType));
                return resultType;
            }

            case LOAD_ARG: {
                Term.Variable loadArgument = (Term.Variable) term;
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
            case GET: {
                Term.Get get = (Term.Get) term;
                List<Type> typeArguments = get.typeArguments
                        .stream()
                        .map((e) -> resolve(solutions, e))
                        .collect(Collectors.toList());
                Type type = resolve(solutions, get.type);
                return new Term.Get(get.library, get.name, type, typeArguments);
            }

            case APPLY: {
                Term.Apply apply = (Term.Apply) term;
                Term function = decorate(solutions, apply.function);
                List<Term> arguments = apply.arguments
                        .stream()
                        .map((e) -> decorate(solutions, e))
                        .collect(Collectors.toList());
                return new Term.Apply(function, arguments);
            }

            case LOAD_ARG: {
                Term.Variable loadArgument = (Term.Variable) term;
                Type type = resolve(solutions, loadArgument.type);
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

        Type.Concrete concrete = (Type.Concrete) type;
        if (concrete.tag.arity == 0) {
            return type;
        }

        Type[] replaced = Arrays.stream(concrete.types)
                .map((e) -> resolve(solutions, e))
                .toArray(Type[]::new);
        return new Type.Concrete(concrete.tag, replaced);
    }

}
