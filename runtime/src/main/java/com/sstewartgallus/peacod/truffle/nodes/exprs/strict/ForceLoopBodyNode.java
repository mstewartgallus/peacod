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
package com.sstewartgallus.peacod.truffle.nodes.exprs.strict;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.sstewartgallus.peacod.truffle.nodes.BaseNode;
import com.sstewartgallus.peacod.truffle.runtime.Thunk;

abstract class ForceLoopBodyNode extends BaseNode {
    ForceLoopBodyNode() {
    }

    static ForceLoopBodyNode uninit() {
        return new Uninit();
    }

    abstract Object executeGeneric(VirtualFrame frame, CallTarget target, Object[] state);

    static final class Uninit extends ForceLoopBodyNode {
        @Override
        public Object executeGeneric(VirtualFrame frame, CallTarget target, Object[] state) {
            Object obj = target.call(state);
            CompilerDirectives.transferToInterpreterAndInvalidate();
            replace(new Monomorphic(target));
            return obj;
        }

    }

    static final class Monomorphic extends ForceLoopBodyNode {
        private final CallTarget cachedTarget;

        @Node.Child
        private DirectCallNode callNode;

        Monomorphic(CallTarget target) {
            this.cachedTarget = target;
            this.callNode = Truffle.getRuntime().createDirectCallNode(target);
        }

        // fixme specialize on type?
        @Override
        public Object executeGeneric(VirtualFrame frame, CallTarget target, Object[] state) {
            if (target != this.cachedTarget) {
                return invalidate(target, state);
            }
            return callNode.call(state);
        }

        private Object invalidate(CallTarget target, Object[] state) {
            Object obj = target.call(state);
            CompilerDirectives.transferToInterpreterAndInvalidate();
            replace(new Polymorphic());
            return obj;
        }
    }

    static final class Polymorphic extends ForceLoopBodyNode {
        @Node.Child
        private IndirectCallNode indirect;

        Polymorphic() {
            this.indirect = Truffle.getRuntime().createIndirectCallNode();
        }

        @Override
        public Object executeGeneric(VirtualFrame frame, CallTarget target, Object[] state) {
            return indirect.call(target, state);
        }
    }
}
