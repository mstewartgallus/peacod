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
package com.sstewartgallus.peacod.truffle.nodes.exprs.intrinsics;

import com.sstewartgallus.peacod.truffle.Libraries;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;

import java.util.HashMap;
import java.util.Map;

public class BuiltinIntrinsics {
    // FIXME lazily initialize?
    public static final Map<String, Libraries.Intrinsic> INTRINSICS = new HashMap<>();

    static {
        INTRINSICS.put("if", (arguments, types) -> {
            TypeNode type = types.get(0);
            PolyNode cond = arguments.get(0);
            PolyNode onCond = arguments.get(1);
            PolyNode elseCond = arguments.get(2);

            return IfNode.of(type, cond, onCond, elseCond);
        });
    }
}
