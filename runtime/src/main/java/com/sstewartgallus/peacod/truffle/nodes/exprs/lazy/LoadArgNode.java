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
package com.sstewartgallus.peacod.truffle.nodes.exprs.lazy;

import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.runtime.PeacodType;
import com.sstewartgallus.peacod.truffle.runtime.Thunk;

@ImportStatic({NodeUtils.class})
public abstract class LoadArgNode extends LazyNode {
    private final int index;

    // FIXME type will tell whether boxed or unboxed etc..
    @Executed
    @Child
    TypeNode type;

    // FIXME... cache update already run results in a frame?
    LoadArgNode(int index, TypeNode type) {
        this.index = index;
        this.type = type;
    }

    public static PolyNode of(int index, TypeNode type) {
        return LoadArgNodeGen.create(index, type);
    }

    @Specialization
    Thunk getThunk(VirtualFrame frame, PeacodType type) {
        return (Thunk) frame.getArguments()[index];
    }
}
