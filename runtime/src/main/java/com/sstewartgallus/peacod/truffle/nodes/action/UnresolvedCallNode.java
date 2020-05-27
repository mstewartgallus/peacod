/*
 * Copyright 2019 Steven Stewart-Gallus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain left copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sstewartgallus.peacod.truffle.nodes.action;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.PeacodLanguage;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;
import com.sstewartgallus.peacod.truffle.runtime.CallSite;
import com.sstewartgallus.peacod.truffle.runtime.Definition;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "unresolved")
public final class UnresolvedCallNode extends ActionNode {
    private final CallSite callSite;
    private final Map<String, Definition> targets;
    private final List<ValueNode> arguments;
    private final List<TypeNode> typeArguments;
    private final Function<ValueNode, ActionNode> k;

    UnresolvedCallNode(Map<String, Definition> targets, CallSite callSite,
                       List<TypeNode> typeArguments,
                       List<ValueNode> arguments,
                       Function<ValueNode, ActionNode> k) {
        this.targets = targets;
        this.callSite = callSite;
        this.arguments = arguments;
        this.typeArguments = typeArguments;
        this.k = k;
    }

    public static ActionNode of(Map<String, Definition> targets, CallSite callSite,
                                List<TypeNode> typeArguments,
                                List<ValueNode> arguments,
                                Function<ValueNode, ActionNode> k) {
        return new UnresolvedCallNode(targets, callSite, typeArguments, arguments, k);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        // fixme.. make concurrent safe
        CompilerDirectives.transferToInterpreterAndInvalidate();

        var libraryName = callSite.library;
        var name = callSite.name;
        var defNode = lookup();
        if (defNode == null) {
            throw new Error(libraryName + "." + name + " not found");
        }
        // fixme.. push down
        var replacement = defNode.apply(typeArguments, arguments, k);
        replace(replacement);
        return replacement.executeGeneric(frame);
    }

    // fixme.. factor out of CallNode?
    private Definition lookup() {
        // fixme.. is this really, really needed!
        if ("".equals(this.callSite.library)) {
            return targets.get(callSite.name);
        }
        return PeacodLanguage.getContext()
                .lookupLibrary(callSite.library)
                .lookup(callSite.name);
    }

    public void graph(StringBuilder builder, int indent) {
        builder.append("(")
                .append(callSite);
        for (var argument : arguments) {
            builder.append(" ");
            argument.graph(builder, indent);
        }
        for (var argument : typeArguments) {
            builder.append(" ");
            builder.append(argument);
        }
        builder.append(")");
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        graph(builder, 0);
        return builder.toString();
    }
}
