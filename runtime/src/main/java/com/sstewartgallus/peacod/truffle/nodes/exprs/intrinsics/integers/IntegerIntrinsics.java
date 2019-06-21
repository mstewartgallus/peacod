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
package com.sstewartgallus.peacod.truffle.nodes.exprs.intrinsics.integers;

import com.sstewartgallus.peacod.truffle.Libraries;

import java.util.HashMap;
import java.util.Map;

public class IntegerIntrinsics {
    // FIXME lazily initialize?
    public static final Map<String, Libraries.Intrinsic> INTRINSICS = new HashMap<>();
    static {
        // fixme give better names symbols?
        INTRINSICS.put("intAdd", (arguments, types) -> AddNodeGen.create(arguments.get(0), arguments.get(1)));
        INTRINSICS.put("intSub", (arguments, types) -> SubNodeGen.create(arguments.get(0), arguments.get(1)));
        INTRINSICS.put("intMul", (arguments, types) -> MulNodeGen.create(arguments.get(0), arguments.get(1)));
        INTRINSICS.put("intLessThan", (arguments, types) -> LessThanNodeGen.create(arguments.get(0), arguments.get(1)));
    }
}
