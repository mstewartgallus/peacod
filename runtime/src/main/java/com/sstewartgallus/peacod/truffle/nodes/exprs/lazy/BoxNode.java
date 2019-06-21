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
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.exprs.strict.StrictNode;
import com.sstewartgallus.peacod.truffle.runtime.Thunk;


@NodeInfo(shortName = "box")
@ImportStatic({NodeUtils.class})
public abstract class BoxNode extends LazyNode {

    @Executed
    @Child
    StrictNode body;

    BoxNode(StrictNode body) {
        this.body = body;
    }

    public static BoxNode of(StrictNode body) {
        return BoxNodeGen.create(body);
    }

    @Specialization
    Thunk intBox(VirtualFrame frame, int value) {
        throw new ThunkIntReturnException(value);
    }

    @Fallback
    Thunk fallback(VirtualFrame frame, Object obj) {
        throw new ThunkReturnException(obj);
    }
}
