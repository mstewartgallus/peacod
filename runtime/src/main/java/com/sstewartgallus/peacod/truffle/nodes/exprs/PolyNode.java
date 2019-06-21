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
package com.sstewartgallus.peacod.truffle.nodes.exprs;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.sstewartgallus.peacod.truffle.PeacodTypesGen;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.LazyNode;
import com.sstewartgallus.peacod.truffle.nodes.exprs.strict.StrictNode;

public abstract class PolyNode extends ExprNode {
    public abstract StrictNode force();

    public abstract LazyNode box();

    public boolean executeBoolean(VirtualFrame frame) {
        return PeacodTypesGen.asBoolean(execute(frame));
    }

    public byte executeByte(VirtualFrame frame) {
        return PeacodTypesGen.asByte(execute(frame));
    }

    public short executeShort(VirtualFrame frame) {
        return PeacodTypesGen.asShort(execute(frame));
    }

    public int executeInteger(VirtualFrame frame) {
        return PeacodTypesGen.asInteger(execute(frame));
    }

    public long executeLong(VirtualFrame frame) {
        return PeacodTypesGen.asLong(execute(frame));
    }

    public abstract Object execute(VirtualFrame frame);

}
