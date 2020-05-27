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

import com.sstewartgallus.peacod.ast.Expr;
import com.sstewartgallus.peacod.ast.LibraryDef;
import com.sstewartgallus.peacod.ast.TypeExpr;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.PushActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.UnresolvedCallNode;
import com.sstewartgallus.peacod.truffle.nodes.type.LiteralNode;
import com.sstewartgallus.peacod.truffle.nodes.type.LoadTypeNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.nodes.value.*;
import com.sstewartgallus.peacod.truffle.runtime.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Libraries {

    private Libraries() {
    }

    public static Lib loadLibrary(LibraryDef ast) {
        Map<String, Definition> targets = new HashMap<>();

        System.err.println(ast);

        var version = ast.getVersion();
        if (version != 1) {
            throw new Error("unknown library version " + version);
        }

        for (var entry : ast.getDefMap().entrySet()) {
            var name = entry.getKey();
            var def = entry.getValue();

            var scheme = def.getType();

            var type = scheme.getType();
            // fixme check if actually function
            // fixme.. awkward incorrect for 0 argument..
            var numArgs = def.getArity();
            var numTypeParams = scheme.getArity();

            var body = def.getBody();

            var context = new ContextInfo(targets, name);

            for (var ii = 0; ii < numArgs; ++ii) {
                context.argument();
            }
            for (var ii = 0; ii < numTypeParams; ++ii) {
                context.typeArgument();
            }

            var typeNode = toTypeNode(context, type);

            var bodyNode = toNodeCps(context, body, PushActionNode::of);

            var defRt = Definition.of(name, numTypeParams, numArgs, typeNode, bodyNode);

            System.err.println(defRt);

            targets.put(name, defRt);
        }

        return Lib.of(targets);
    }

    private static TypeNode toTypeNode(ContextInfo context, TypeExpr expr) {
        switch (expr.getValueCase()) {
            case VARIABLE: {
                var index = expr.getVariable().getIndex();
                return LoadTypeNode.of(index);
            }

            case LITERAL: {
                var literal = expr.getLiteral();
                var name = literal.getName();
                var params = literal
                        .getArgumentList()
                        .stream()
                        .map(t -> Libraries.toTypeNode(context, t))
                        .collect(Collectors.toList());
                return LiteralNode.of(name, params);
            }

            case VALUE_NOT_SET:
                throw new Error("no value set");

            default:
                throw new Error("unimpl " + expr);
        }
    }

    private static ActionNode toNodeCps(ContextInfo context, Expr expr, Function<ValueNode, ActionNode> k) {
        switch (expr.getValueCase()) {
            case VARIABLE: {
                var loadArg = expr.getVariable();
                var type = toTypeNode(context, loadArg.getType());
                var index = loadArg.getIndex();
                return k.apply(ArgumentNode.of(type, index));
            }

            // fixme... have constant f...
            case CALL: {
                var call = expr.getCall();

                // fixme...
                var function = call.getFunction().getReference();

                List<ValueNode> arguments = new ArrayList<>();
                var argList = new ArrayList<>(call.getArgumentList());
                return doCall(context, k, call, function, arguments, argList);
            }

            case CONSTANT: {
                var constant = expr.getConstant();
                switch (constant.getValueCase()) {
                    // fixme.. can strictness be used here as well?
                    case REFERENCE: {
                        var call = constant.getReference();

                        var ref = toCallSite(context, call);

                        return k.apply(UnresolvedNode.of(context.targets, ref));
                    }
                    case LITERAL: {
                        var lit = constant.getLiteral();
                        switch (lit.getValueCase()) {
                            case LITERAL_TRUE:
                                return k.apply(BooleanValueNode.of(true));
                            case LITERAL_FALSE:
                                return k.apply(BooleanValueNode.of(false));
                            case LITERAL_BYTE: {
                                var value = lit.getLiteralByte().getValue();
                                return k.apply(IntValueNode.of(value));
                            }
                            case LITERAL_SHORT: {
                                var value = lit.getLiteralShort().getValue();
                                return k.apply(IntValueNode.of(value));
                            }
                            case LITERAL_INT: {
                                var value = lit.getLiteralInt().getValue();
                                return k.apply(IntValueNode.of(value));
                            }
                            case LITERAL_LONG: {
                                var value = lit.getLiteralLong().getValue();
                                return k.apply(LongValueNode.of(value));
                            }

                            case VALUE_NOT_SET:
                                throw new Error("no value set");
                        }
                    }

                    case VALUE_NOT_SET:
                        throw new Error("no value set");
                }
            }

            case VALUE_NOT_SET:
                throw new Error("no value set");

            default:
                throw new Error("unimpl " + expr);
        }
    }

    private static ActionNode doCall(ContextInfo context, Function<ValueNode, ActionNode> k, Expr.Call call, Expr.TermReference function, List<ValueNode> arguments, List<Expr> argList) {
        if (argList.isEmpty()) {
            var typeArguments = call
                    .getTypeArgumentList()
                    .stream()
                    .map(t -> Libraries.toTypeNode(context, t))
                    .collect(Collectors.toList());

            return UnresolvedCallNode.of(context.targets, toCallSite(context, function), typeArguments, arguments, k);

        }
        return toNodeCps(context, argList.remove(0), arg -> {
            arguments.add(arg);
            return doCall(context, k, call, function, arguments, argList);
        });
    }

    private static CallSite toCallSite(ContextInfo context, Expr.TermReference ref) {
        var library = ref.getReference().getLibrary();
        var name = ref.getReference().getName();
        var type = ref.getScheme();
        return new CallSite(library, name);
    }


    private static final class ContextInfo {
        final String name;
        final List<TypeArgument> typeArguments;
        final List<Variable> arguments;
        final Map<String, Definition> targets;

        private ContextInfo(Map<String, Definition> targets, String name) {
            this.typeArguments = new ArrayList<>();
            this.arguments = new ArrayList<>();
            this.targets = targets;
            this.name = name;
        }

        TypeArgument typeArgument() {
            var arg = new TypeArgument();
            typeArguments.add(arg);
            return arg;
        }

        Variable argument() {
            var arg = new Variable();
            arguments.add(arg);
            return arg;
        }

    }
}
