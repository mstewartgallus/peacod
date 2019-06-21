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
package com.sstewartgallus.peacod.truffle.nodes;

import com.sstewartgallus.peacod.truffle.runtime.PeacodType;

public class NodeUtils {
    public static boolean isBoolean(PeacodType t) {
        return PeacodType.BOOLEAN_VALUE == t;
    }

    public static boolean isByte(PeacodType t) {
        return PeacodType.BYTE_VALUE == t;
    }

    public static boolean isShort(PeacodType t) {
        return PeacodType.SHORT_VALUE == t;
    }

    public static boolean isInt(PeacodType t) {
        return PeacodType.INT_VALUE == t;
    }

    public static boolean isLong(PeacodType t) {
        return PeacodType.LONG_VALUE == t;
    }
}
