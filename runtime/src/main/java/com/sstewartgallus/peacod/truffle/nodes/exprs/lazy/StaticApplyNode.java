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
package com.sstewartgallus.peacod.truffle.nodes.exprs.lazy;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.sstewartgallus.peacod.truffle.PeacodLanguage;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.PeacodLazyRootNode;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;
import com.sstewartgallus.peacod.truffle.nodes.exprs.strict.StrictNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.runtime.Definition;
import com.sstewartgallus.peacod.truffle.runtime.Spec;
import com.sstewartgallus.peacod.truffle.runtime.Thunk;

import java.util.List;
import java.util.Map;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "apply")
public abstract class StaticApplyNode extends LazyNode {

    public static PolyNode of(Map<String, Definition> targets,
                              String name, List<TypeNode> typeArguments, List<PolyNode> arguments) {
        return new Uninit(name, targets, typeArguments.toArray(new TypeNode[0]), arguments);
    }

    static final class Uninit extends LazyNode {
        private final List<PolyNode> arguments;

        @Children
        private final TypeNode[] typeArguments;

        private final String name;
        private final Map<String, Definition> targets;

        Uninit(String name, Map<String, Definition> targets, TypeNode[] typeArguments, List<PolyNode> arguments) {
            this.arguments = arguments;
            this.typeArguments = typeArguments;
            this.name = name;
            this.targets = targets;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object[] args = new Object[typeArguments.length];
            for (int ii = 0; ii < typeArguments.length; ++ii) {
                // FIXME... doesn't work if type changes
                args[ii] = typeArguments[ii].execute(frame);
            }

            RootCallTarget target = ((Spec) targets.get(name).produceProcedure.call(args)).target;
            CompilerDirectives.transferToInterpreterAndInvalidate();

            RootCallTarget[] lazyArgs = arguments
                    .stream()
                    .map(e ->
                            Truffle
                                    .getRuntime()
                                    .createCallTarget(new Stub(e))
                    ).toArray(RootCallTarget[]::new);

            PolyNode newNode;
            if (target.getRootNode() instanceof PeacodLazyRootNode) {
                newNode = new InitLazy(target, lazyArgs);
            } else {
                newNode = new InitStrict(target, lazyArgs);
            }
            replace(newNode);
            return newNode.execute(frame);
        }
    }

    static final class InitStrict extends StrictNode {

        private final RootCallTarget[] arguments;
        @Child
        private DirectCallNode function;

        InitStrict(CallTarget target, RootCallTarget[] arguments) {
            this.function = Truffle.getRuntime().createDirectCallNode(target);
            this.arguments = arguments;
        }

        @Override
        @ExplodeLoop
        public Object execute(VirtualFrame frame) {
            Object[] args = new Object[arguments.length];
            for (int ii = 0; ii < arguments.length; ++ii) {
                // FIXME... is the boxing really that bad?
                args[ii] = Thunk.of(arguments[ii], frame);
            }
            return function.call(args);
        }
    }

    static final class InitLazy extends LazyNode {

        private final RootCallTarget[] arguments;
        @Child
        private DirectCallNode function;

        InitLazy(CallTarget target, RootCallTarget[] arguments) {
            this.function = Truffle.getRuntime().createDirectCallNode(target);
            this.arguments = arguments;
        }

        @Override
        @ExplodeLoop
        public Object execute(VirtualFrame frame) {
            Object[] args = new Object[arguments.length];
            for (int ii = 0; ii < arguments.length; ++ii) {
                // FIXME... is the boxing really that bad?
                args[ii] = Thunk.of(arguments[ii], frame);
            }
            return function.call(args);
        }
    }

    static final class Stub extends RootNode {

        @Child
        private PolyNode body;

        Stub(PolyNode body) {
            super(PeacodLanguage.getLanguage());
            this.body = body;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return body.execute(frame);
        }
    }
}
