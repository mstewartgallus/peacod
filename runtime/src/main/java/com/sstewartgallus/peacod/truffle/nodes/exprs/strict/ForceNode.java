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

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.ThunkIntReturnException;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.ThunkReturnException;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.LazyNode;
import com.sstewartgallus.peacod.truffle.runtime.Thunk;


@NodeInfo(shortName = "!")
@ImportStatic({NodeUtils.class})
public final class ForceNode extends StrictNode {

    @Child
    LazyNode body;

    @Child
    ForceLoopBodyNode loopBodyNode;

    private ForceNode(LazyNode body) {
        this.body = body;
        this.loopBodyNode = ForceLoopBodyNode.uninit();
    }

    public static ForceNode of(LazyNode body) {
        return new ForceNode(body);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object object = body.execute(frame);
        if (!(object instanceof Thunk)) {
            return object;
        }
        Thunk thunk = (Thunk)object;

        CallTarget target = thunk.target;
        Object[] state = thunk.state;
        for (;;) {
            object = loopBodyNode.executeGeneric(frame, target, state);
            if (!(object instanceof Thunk)) {
                return object;
            }
            thunk = (Thunk)object;
            target = thunk.target;
            state = thunk.state;
        }
    }
}
