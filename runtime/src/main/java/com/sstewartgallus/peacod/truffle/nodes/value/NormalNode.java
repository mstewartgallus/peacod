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

import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.plato.ApplyTerm;
import com.sstewartgallus.peacod.truffle.plato.Term;

import java.util.Objects;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "!")
public abstract class NormalNode extends ValueNode {
    @Executed
    @Child
    ValueNode child;

    NormalNode(ValueNode child) {
        this.child = Objects.requireNonNull(child);
    }

    public static NormalNode of(ValueNode child) {
        return NormalNodeGen.create(child);
    }

    // slow path...
    @Specialization
    final Term<?> forceFallback(Term<?> term) {
        return force(term);
    }

    private Term<?> force(Term<?> term) {
        while (term instanceof ApplyTerm) {
            var apply = (ApplyTerm<?, ?>) term;
            term = deepen(apply);
        }
        return term;
    }

    private <A, B> Term<B> deepen(ApplyTerm<A, B> apply) {
        throw null;
    }
}
