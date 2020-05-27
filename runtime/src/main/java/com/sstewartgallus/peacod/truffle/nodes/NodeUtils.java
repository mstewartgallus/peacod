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

import com.sstewartgallus.peacod.truffle.PeacodContext;
import com.sstewartgallus.peacod.truffle.runtime.PeacodTypeInstance;
import com.sstewartgallus.peacod.truffle.runtime.Usage;

public class NodeUtils {
    public static boolean isStrict(Usage strictness) {
        return strictness == Usage.STRICT;
    }

    public static boolean isBoolean(PeacodTypeInstance t) {
        return PeacodContext.BOOLEAN_VALUE == t.parent;
    }

    public static boolean isByte(PeacodTypeInstance t) {
        return PeacodContext.BYTE_VALUE == t.parent;
    }

    public static boolean isShort(PeacodTypeInstance t) {
        return PeacodContext.SHORT_VALUE == t.parent;
    }

    public static boolean isInt(PeacodTypeInstance t) {
        return PeacodContext.INT_VALUE == t.parent;
    }

    public static boolean isLong(PeacodTypeInstance t) {
        return PeacodContext.LONG_VALUE == t.parent;
    }
}
