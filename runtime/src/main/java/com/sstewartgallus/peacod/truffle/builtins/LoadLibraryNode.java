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
package com.sstewartgallus.peacod.truffle.builtins;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.RootNode;
import com.sstewartgallus.peacod.ast.LibraryDef;
import com.sstewartgallus.peacod.truffle.Libraries;
import com.sstewartgallus.peacod.truffle.PeacodLanguage;
import com.sstewartgallus.peacod.truffle.runtime.Lib;

public abstract class LoadLibraryNode extends RootNode {

    private LibraryDef ast;

    public LoadLibraryNode(LibraryDef library) {
        super(PeacodLanguage.getLanguage());
        // FIXME: Verify?
        ast = library;
    }

    @Override
    protected boolean isInstrumentable() {
        return false;
    }

    // FIXME...
    @Override
    public String getName() {
        return "root eval";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Specialization
    public Lib loadLibrary(@Cached("load()") Lib library) {
        return library;
    }

    Lib load() {
        // FIXME: Use some kind of lambda passing in
        // this should be a builtin not a nodes
        var library = Libraries.loadLibrary(ast);
        ast = null;
        return library;
    }
}
