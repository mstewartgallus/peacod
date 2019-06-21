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

import java.util.*;

class Serializer {
    static LibraryDef serializeLibrary(Map<String, TypedDefinition> definitions) {
        LibraryDef.Builder builder = LibraryDef
                .newBuilder()
                .setVersion(1)
                .setName("TheModuleNameTODO");

        for (Map.Entry<String, TypedDefinition> expr : definitions.entrySet()) {
            String name = expr.getKey();
            TypedDefinition def = expr.getValue();

            Type type = def.type;
            List<Type> typeArguments = def.typeArguments;
            Term body = def.expr;

            Map<Type, Integer> map = new HashMap<>();
            {
                int ii = 0;
                for (Type argument : typeArguments) {
                    map.put(argument, ii++);
                }
            }

            TypeExpr serializedType = serializeType(map, type);
            Expr serialized = serialize(map, body);

            FunctionDef.Builder func = FunctionDef
                    .newBuilder()
                    .setName(name)
                    .setType(serializedType)
                    .setNumTypeParams(typeArguments.size())
                    .setBody(serialized);

            builder.addFunction(func);
        }

        return builder.build();
    }

    // FIXME Serialize declare constructors/tags more directly
    private static TypeExpr serializeType(Map<Type, Integer> map, Type expr) {
        if (expr instanceof Type.Hole) {
            return TypeExpr
                    .newBuilder()
                    .setVariable(
                            TypeExpr.Variable
                                    .newBuilder()
                                    .setIndex(map.get(expr))
                                    .build())
                    .build();
        }

        Type.Concrete conc = (Type.Concrete) expr;

        Type.Tag tag = conc.tag;
        String libraryName = tag.library;
        String name = tag.name;
        Type[] types = conc.types;

        TypeExpr.Literal.Builder literal = TypeExpr.Literal
                .newBuilder();

        literal.setLibrary(libraryName);
        literal.setName(name);

        for (Type type : types) {
            TypeExpr serialized = serializeType(map, type);
            literal.addArgument(serialized);
        }

        return TypeExpr
                .newBuilder()
                .setLiteral(literal)
                .build();
    }

    private static Expr serialize(Map<Type, Integer> map, Term expr) {
        switch (expr.tag()) {
            case GET: {
                Term.Get get = (Term.Get) expr;

                String library = get.library;
                TypeExpr type = serializeType(map, get.type);

                Expr.Get.Builder builder =
                        Expr.Get
                                .newBuilder()
                                .setType(type)
                                .setName(get.name);

                if (library != null) {
                    builder.setLibrary(library);
                }

                for (Type t : get.typeArguments) {
                    builder.addTypeArgument(serializeType(map, t));
                }
                return Expr
                        .newBuilder()
                        .setGet(builder)
                        .build();
            }

            case APPLY: {
                Term.Apply apply = (Term.Apply) expr;

                Expr function = serialize(map, apply.function);

                Expr.Apply.Builder builder =
                        Expr.Apply
                                .newBuilder()
                                .setFunction(function);

                for (Term val : apply.arguments) {
                    builder.addArgument(serialize(map, val));
                }

                return Expr
                        .newBuilder()
                        .setApply(builder)
                        .build();
            }
            case LOAD_ARG: {
                Term.Variable loadArg = (Term.Variable) expr;
                TypeExpr type = serializeType(map, loadArg.type);
                int argIndex = loadArg.argIndex;
                return Expr
                        .newBuilder()
                        .setLoadArg(
                                Expr.LoadArg
                                        .newBuilder()
                                        .setType(type)
                                        .setIndex(argIndex))
                        .build();
            }

            case LIT_BOOLEAN: {
                Term.LitBoolean litBoolean = (Term.LitBoolean) expr;
                return Expr
                        .newBuilder()
                        .setSimple(litBoolean.value ? Expr.Simple.TRUE : Expr.Simple.FALSE)
                        .build();
            }

            case LIT_INT: {
                Term.LitInt litInt = (Term.LitInt) expr;
                return Expr
                        .newBuilder()
                        .setLitInt(
                                Expr.LitInt
                                        .newBuilder()
                                        .setValue(litInt.value)
                                        .build())
                        .build();
            }

            case LIT_LONG: {
                Term.LitLong litInt = (Term.LitLong) expr;
                return Expr
                        .newBuilder()
                        .setLitLong(
                                Expr.LitLong
                                        .newBuilder()
                                        .setValue(litInt.value)
                                        .build())
                        .build();
            }
            default:
                throw new Error("unknown tag " + expr.tag());
        }
    }
}
