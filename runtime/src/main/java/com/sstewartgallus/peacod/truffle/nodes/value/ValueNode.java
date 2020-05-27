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

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.sstewartgallus.peacod.truffle.PeacodTypesGen;
import com.sstewartgallus.peacod.truffle.cbpv.Value;
import com.sstewartgallus.peacod.truffle.nodes.BaseNode;

public abstract class ValueNode extends BaseNode {
    // fixme.. specialize thunks...
    public abstract Object execute(VirtualFrame frame);

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return PeacodTypesGen.expectBoolean(execute(frame));
    }

    public int executeInteger(VirtualFrame frame) throws UnexpectedResultException {
        return PeacodTypesGen.expectInteger(execute(frame));
    }

    public Value<?> executeValue(VirtualFrame frame) throws UnexpectedResultException {
        return PeacodTypesGen.expectValue(execute(frame));
    }

    public void graph(StringBuilder builder, int indent) {
        builder.append(toString());
    }

}
