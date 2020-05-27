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
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.cbpv.PushAction;
import com.sstewartgallus.peacod.truffle.cbpv.Value;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "push")
public abstract class PushActionNode extends ActionNode {
    @Executed
    @Node.Child
    ValueNode value;

    PushActionNode(ValueNode value) {
        this.value = value;
    }

    public static PushActionNode of(ValueNode value) {
        return PushActionNodeGen.create(value);
    }

    @Specialization
    Action<?> push(Value<?> x) {
        return new PushAction<>(x);
    }

    @Specialization
    int push(int x) {
        return x;
    }

    @Override
    public String toString() {
        return "(push " + value + ")";
    }
}
