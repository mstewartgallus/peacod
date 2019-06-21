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

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

import java.util.Map;

@ExportLibrary(InteropLibrary.class)
@SuppressWarnings("static-method")
public final class Library extends PeacodObject {

    private final Map<String, Definition> defs;

    // FIXME: Perhaps the builder pattern would be better?
    private Library(Map<String, Definition> defs) {
        this.defs = defs;
    }

    public static Library of(Map<String, Definition> defs) {
        return new Library(defs);
    }

    @ExportMessage
    boolean hasMembers() {
        return !defs.isEmpty();
    }

    @ExportMessage
    @TruffleBoundary
    Definition readMember(String member) {
        return defs.get(member);
    }

    @ExportMessage
    @TruffleBoundary
    boolean isMemberReadable(String member) {
        return defs.containsKey(member);
    }

    @ExportMessage
    @TruffleBoundary
    Object getMembers(@SuppressWarnings("unused") boolean includeInternal) {
        return new ProceduresObject(defs.keySet().toArray());
    }

    @Override
    public Object findMetaObject() {
        return "Library";
    }

}
