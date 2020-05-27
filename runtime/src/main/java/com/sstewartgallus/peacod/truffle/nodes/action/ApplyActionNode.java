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
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.cbpv.ApplyAction;
import com.sstewartgallus.peacod.truffle.cbpv.Value;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "@")
public abstract class ApplyActionNode extends ActionNode {
    @Executed
    @Child
    ActionNode function;

    @Executed
    @Child
    ValueNode argument;

    ApplyActionNode(ActionNode function, ValueNode argument) {
        this.function = function;
        this.argument = argument;
    }

    public static ApplyActionNode of(ActionNode function, ValueNode argument) {
        return ApplyActionNodeGen.create(function, argument);
    }

    // fixme... check types... ?
    @Specialization
    @ExplodeLoop
    ApplyAction<?, ?> call(VirtualFrame frame, Action<?> f) {
        var x = argument.execute(frame);
        return new ApplyAction(f, (Value) x);
    }
}
