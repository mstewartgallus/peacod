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
package com.sstewartgallus.peacod.truffle.nodes.action.intrinsics;

import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "if")
public abstract class IfNode extends ActionNode {
    @Executed
    @Child
    ValueNode cond;
    @Child
    ActionNode left;
    @Child
    ActionNode right;

    IfNode(ValueNode cond, ActionNode left, ActionNode right) {
        this.cond = cond;
        this.left = left;
        this.right = right;
    }

    public static IfNode of(ValueNode cond, ActionNode left, ActionNode right) {
        return IfNodeGen.create(cond, left, right);
    }

    @Override
    public String toString() {
        return "(if " + left + " " + right + ")";
    }

    @Specialization
    final Object ifCond(VirtualFrame frame, boolean cond) {
        if (cond) {
            return left.executeGeneric(frame);
        } else {
            return right.executeGeneric(frame);
        }
    }
}
