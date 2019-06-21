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
package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;

// FIXME, consider separate classes for different types, IntThunk and so on
@CompilerDirectives.ValueType
public final class Thunk implements TruffleObject {
    public final CallTarget target;
    public final Object[] state;

    public Thunk(CallTarget target, Object[] state) {
        this.target = target;
        this.state = state;
    }

    public static Thunk of(CallTarget target, Object[] state) {
        return new Thunk(target, state);
    }

    public static Thunk of(CallTarget target, VirtualFrame frame) {
        return new Thunk(target, frame.getArguments());
    }
}
