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

import com.sstewartgallus.peacod.ast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Serializer {
    static LibraryDef serializeLibrary(Map<String, TypedDefinition> definitions) {
        var builder = LibraryDef
                .newBuilder()
                .setVersion(1)
                .setName("TheModuleNameTODO");

        for (var expr : definitions.entrySet()) {
            var name = expr.getKey();
            var def = expr.getValue();

            var type = def.scheme;
            var typeArguments = def.scheme.holes;
            var body = def.expr;

            Map<Type, Integer> map = new HashMap<>();
            {
                var ii = 0;
                for (Type argument : typeArguments) {
                    map.put(argument, ii++);
                }
            }

            var serializedType = serializeType(type);
            var serialized = serialize(map, body);

            builder.putDef(name, Def
                    .newBuilder()
                    .setType(serializedType)
                    .setArity(def.arity)
                    .setBody(serialized)
                    .build());
        }

        return builder.build();
    }

    private static TypeSchemeExpr serializeType(TypeScheme scheme) {
        Map<Type, Integer> holeMap = new HashMap<>();
        var ii = 0;
        for (var hole : scheme.holes) {
            holeMap.put(hole, ii);
            ++ii;
        }
        var type = serializeType(holeMap, scheme.type);

        return TypeSchemeExpr.newBuilder()
                .setArity(scheme.holes.size())
                .setType(type)
                .build();
    }

    // FIXME Serialize declare constructors/tags more directly
    private static TypeExpr serializeType(Map<Type, Integer> map, Type type) {
        Objects.requireNonNull(map);
        if (type instanceof Type.Hole) {
            Objects.requireNonNull(type);
            int index = map.getOrDefault(type, -1);
            if (index < 0) {
                throw new Error("hole " + type + " not found in " + map);
            }
            return TypeExpr
                    .newBuilder()
                    .setVariable(
                            TypeExpr.Variable
                                    .newBuilder()
                                    .setIndex(index)
                                    .build())
                    .build();
        }

        var conc = (Type.Concrete) type;

        var tag = conc.tag;
        var libraryName = tag.library;
        var name = tag.name;
        var types = conc.types;

        var literal = TypeExpr.Literal
                .newBuilder();

        literal.setLibrary(libraryName);
        literal.setName(name);

        for (var argumentType : types) {
            var serialized = serializeType(map, argumentType);
            literal.addArgument(serialized);
        }

        return TypeExpr
                .newBuilder()
                .setLiteral(literal)
                .build();
    }

    private static Expr serialize(Map<Type, Integer> map, Term expr) {
        switch (expr.tag()) {
            case APPLY: {
                var apply = (Term.Apply) expr;

                var function = apply.function;
                // fixme... put type arguments in as normal applicatio part...
                var arguments = apply.arguments;

                //fixme
                var funS = serialize(map, function).getConstant();

                var builder = Expr.Call
                        .newBuilder()
                        .setFunction(funS);

                // fixme... add type args...

                for (var val : arguments) {
                    builder.addArgument(serialize(map, val));
                }

                return Expr
                        .newBuilder()
                        .setCall(builder)
                        .build();
            }


            case REFERENCE: {
                var apply = (Term.TermReference) expr;

                var ref = apply.reference;

                var type = serializeType(apply.scheme);

                var builder = Expr.TermReference
                        .newBuilder()
                        .setReference(Expr.Reference
                                .newBuilder()
                                .setLibrary(ref.library)
                                .setName(ref.name))
                        .setScheme(type);

                return Expr
                        .newBuilder()
                        .setConstant(Expr.Constant.newBuilder().setReference(builder))
                        .build();
            }
            case LOAD_ARG: {
                var loadArg = (Term.Variable) expr;
                var type = serializeType(map, loadArg.type);
                var argIndex = loadArg.argIndex;
                return Expr
                        .newBuilder()
                        .setVariable(
                                Expr.Variable
                                        .newBuilder()
                                        .setType(type)
                                        .setIndex(argIndex))
                        .build();
            }

            case LIT_BOOLEAN: {
                var litBoolean = (Term.LitBoolean) expr;
                if (litBoolean.value) {
                    return Expr
                            .newBuilder()
                            .setConstant(Expr.Constant.newBuilder()
                                    .setLiteral(Expr.Literal.newBuilder()
                                            .setLiteralTrue(Expr.LiteralTrue.newBuilder())))
                            .build();
                } else {
                    return Expr
                            .newBuilder()
                            .setConstant(Expr.Constant.newBuilder()
                                    .setLiteral(Expr.Literal.newBuilder()
                                            .setLiteralFalse(Expr.LiteralFalse.newBuilder())))
                            .build();
                }
            }

            case LIT_INT: {
                var litInt = (Term.LitInt) expr;
                return Expr.newBuilder()
                        .setConstant(Expr.Constant.newBuilder()
                                .setLiteral(Expr.Literal
                                        .newBuilder()
                                        .setLiteralInt(
                                                Expr.LiteralInteger
                                                        .newBuilder()
                                                        .setValue(litInt.value))
                                        .build()))
                        .build();
            }

            case LIT_LONG: {
                throw null;
            }
            default:
                throw new Error("unknown tag " + expr.tag());
        }
    }
}
