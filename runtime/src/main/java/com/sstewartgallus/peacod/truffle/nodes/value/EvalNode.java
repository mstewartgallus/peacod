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
package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.LoopConditionProfile;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.cbpv.ApplyAction;
import com.sstewartgallus.peacod.truffle.cbpv.PushAction;
import com.sstewartgallus.peacod.truffle.cbpv.Value;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "eval")
public abstract class EvalNode extends ValueNode {
    @Executed
    @Child
    ActionNode thunk;

    EvalNode(ActionNode thunk) {
        this.thunk = thunk;
    }

    public static EvalNode of(ActionNode thunk) {
        return EvalNodeGen.create(thunk);
    }

    public static LoopConditionProfile mk() {
        return LoopConditionProfile.createCountingProfile();
    }

    @Specialization
    int interpret(int value) {
        return value;
    }

    // fixme... args?
    @Specialization
    Value<?> interpret(Action<?> action, @Cached("mk()") LoopConditionProfile profile) {
        for (; ; ) {
            if (profile.profile(action instanceof ApplyAction)) {
                action = ((ApplyAction<?, ?>) action).step();
                continue;
            }
            if (action instanceof PushAction) {
                return ((PushAction<?>) action).value;
            }
            CompilerAsserts.neverPartOfCompilation();
            throw new Error("todo");
        }
    }

    @Specialization
    Value<?> interpret(Value<?> value) {
        return value;
    }
}
