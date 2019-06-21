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
package com.sstewartgallus.peacod.truffle;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.nodes.RootNode;
import com.sstewartgallus.peacod.ast.Expr;
import com.sstewartgallus.peacod.ast.FunctionDef;
import com.sstewartgallus.peacod.ast.LibraryDef;
import com.sstewartgallus.peacod.ast.TypeExpr;
import com.sstewartgallus.peacod.truffle.nodes.PeacodLazyRootNode;
import com.sstewartgallus.peacod.truffle.nodes.PeacodStrictRootNode;
import com.sstewartgallus.peacod.truffle.nodes.exprs.*;
import com.sstewartgallus.peacod.truffle.nodes.exprs.intrinsics.BuiltinIntrinsics;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.*;
import com.sstewartgallus.peacod.truffle.nodes.exprs.strict.*;
import com.sstewartgallus.peacod.truffle.nodes.exprs.intrinsics.integers.IntegerIntrinsics;
import com.sstewartgallus.peacod.truffle.nodes.type.FunctionNode;
import com.sstewartgallus.peacod.truffle.nodes.type.LoadTypeNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.runtime.*;

import java.util.*;
import java.util.stream.Collectors;

public final class Libraries {

    private static final Map<String, Map<String, Intrinsic>> INTRINSICS;

    static {
        INTRINSICS = new HashMap<>();
        INTRINSICS.put("peacod.lang.Integers", IntegerIntrinsics.INTRINSICS);
        INTRINSICS.put("peacod.lang.Builtin", BuiltinIntrinsics.INTRINSICS);
    }

    private Libraries() {
    }

    public static Library loadLibrary(PeacodLanguage language, LibraryDef ast) {
        Map<String, Definition> targets = new HashMap<>();

        System.err.println(ast);

        int version = ast.getVersion();
        if (version != 1) {
            throw new Error("unknown library version " + version);
        }

        for (FunctionDef function : ast.getFunctionList()) {
            String name = function.getName();

            TypeExpr type = function.getType();
            Expr body = function.getBody();

            ContextInfo context = new ContextInfo();

            PolyNode node = toNode(context, targets, body);

            RootNode root;
            if (node instanceof StrictNode) {
                StrictNode strict = (StrictNode) node;
                root = new PeacodStrictRootNode(name, strict);
            } else if (node instanceof LazyNode) {
                LazyNode lazy = (LazyNode) node;
                root = new PeacodLazyRootNode(name, lazy);
            } else {
                throw new Error("unhandled node case " + node);
            }

            Spec proc = Spec.of(Truffle.getRuntime().createCallTarget(root));
            Definition def =  Definition.of(Truffle.getRuntime().createCallTarget(RootNode.createConstantNode(proc)));
            targets.put(name, def);
        }

        return Library.of(targets);
    }

    private static TypeNode toTypeNode(TypeExpr expr) {
        switch (expr.getValueCase()) {
            case VARIABLE: {
                int index = expr.getVariable().getIndex();
                return LoadTypeNode.of(index);
            }

            case LITERAL: {
                TypeExpr.Literal literal = expr.getLiteral();
                String name = literal.getName();
                List<TypeNode> params = literal
                        .getArgumentList()
                        .stream()
                        .map(Libraries::toTypeNode)
                        .collect(Collectors.toList());
                return TypeNode.ofLiteral(name, params);
            }

            case VALUE_NOT_SET:
                throw new Error("no value set");

            default:
                throw new Error("unimpl " + expr);
        }
    }

    private static PolyNode toNode(ContextInfo context, Map<String, Definition> targets, Expr expr) {
        switch (expr.getValueCase()) {
            case LOAD_ARG: {
                Expr.LoadArg loadArg = expr.getLoadArg();
                TypeNode type = toTypeNode(loadArg.getType());
                return LoadArgNode.of(loadArg.getIndex(), type);
            }

            case GET: {
                throw new Error("unimpl");
            }

            case APPLY: {
                Expr.Apply apply = expr.getApply();

                List<PolyNode> arguments = apply
                        .getArgumentList()
                        .stream()
                        .map((e) -> toNode(context, targets, e))
                        .collect(Collectors.toList());

                // FIXME is this good design?
                Expr funcNode = apply.getFunction();
                if (funcNode.hasGet()) {
                    Expr.Get get = funcNode.getGet();

                    String libraryName = get.getLibrary();
                    String name = get.getName();

                    TypeExpr typeExpr = get.getType();

                    List<TypeNode> typeArguments = get
                            .getTypeArgumentList()
                            .stream()
                            .map(Libraries::toTypeNode)
                            .collect(Collectors.toList());

                    isIntrinsic:
                    {
                        Map<String, Intrinsic> libIntrinsics = INTRINSICS.get(libraryName);
                        if (libIntrinsics == null) {
                            break isIntrinsic;
                        }
                        Intrinsic intrinsic = libIntrinsics.get(name);
                        if (intrinsic == null) {
                            break isIntrinsic;
                        }
                        return intrinsic.makeNode(arguments, typeArguments);
                    }

                    return StaticApplyNode.of(targets, name, typeArguments, arguments);
                }

                // fixme dynamic case
                throw new Error("unimp");
            }

            case SIMPLE:
                switch (expr.getSimple()) {
                    case TRUE:
                        return LiteralBoolean.of(true);

                    case FALSE:
                        return LiteralBoolean.of(false);

                    case UNRECOGNIZED:
                        throw new Error("unrecognized value set");

                    default:
                        throw new Error("no correct value set");
                }

            case LIT_BYTE: {
                Expr.LitByte value = expr.getLitByte();
                return LiteralInt.of(value.getValue());
            }
            case LIT_SHORT: {
                Expr.LitShort value = expr.getLitShort();
                return LiteralInt.of(value.getValue());
            }
            case LIT_INT: {
                Expr.LitInt value = expr.getLitInt();
                return LiteralInt.of(value.getValue());
            }
            case LIT_LONG: {
                Expr.LitLong value = expr.getLitLong();
                return LiteralLong.of(value.getValue());
            }

            case VALUE_NOT_SET:
                throw new Error("no value set");

            default:
                throw new Error("unimpl " + expr);
        }
    }

    public interface Intrinsic {
        PolyNode makeNode(List<PolyNode> arguments, List<TypeNode> typeArguments);
    }

    private static final class ContextInfo {

        private ContextInfo() {
        }
    }
}
