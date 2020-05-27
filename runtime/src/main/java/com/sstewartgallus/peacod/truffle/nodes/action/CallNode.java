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

import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

import java.util.List;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "call")
public abstract class CallNode extends ActionNode {
    @Child
    DirectCallNode function;

    @Executed
    @Children
    ValueNode[] arguments;

    CallNode(DirectCallNode function, ValueNode[] arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public static CallNode of(DirectCallNode function, List<ValueNode> arguments) {
        return CallNodeGen.create(function, arguments.toArray(ValueNode[]::new));
    }

    @Specialization
    @ExplodeLoop
    Object call(VirtualFrame frame) {
        var argValues = new Object[arguments.length];
        for (var ii = 0; ii < arguments.length; ++ii) {
            argValues[ii] = arguments[ii].execute(frame);
        }
        return function.call(argValues);
    }
}
