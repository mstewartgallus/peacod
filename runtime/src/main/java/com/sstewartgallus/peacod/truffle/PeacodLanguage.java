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
package com.sstewartgallus.peacod.truffle;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.ContextPolicy;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.sstewartgallus.peacod.ast.LibraryDef;
import com.sstewartgallus.peacod.compiler.Compiler;
import com.sstewartgallus.peacod.truffle.builtins.LoadLibraryNodeGen;
import com.sstewartgallus.peacod.truffle.runtime.PeacodAny;

import java.io.Reader;
import java.util.List;

@TruffleLanguage.Registration(
        id = PeacodLanguage.ID,
        name = "Peacod",
        defaultMimeType = PeacodLanguage.MIME_TYPE,
        characterMimeTypes = PeacodLanguage.MIME_TYPE,
        contextPolicy = ContextPolicy.SHARED,
        fileTypeDetectors = PeacodFileDetector.class)
@ProvidedTags({
        StandardTags.CallTag.class,
        StandardTags.StatementTag.class,
        StandardTags.RootTag.class,
        StandardTags.ExpressionTag.class,
        DebuggerTags.AlwaysHalt.class})
public final class PeacodLanguage extends TruffleLanguage<PeacodContext> {
    public static final String ID = "peacod";
    public static final String MIME_TYPE = "application/x-peacod";

    public PeacodLanguage() {
    }

    public static PeacodLanguage getLanguage() {
        return getCurrentLanguage(PeacodLanguage.class);
    }

    @Override
    protected PeacodContext createContext(Env env) {
        return new PeacodContext();
    }

    @Override
    protected CallTarget parse(ParsingRequest request) {
        Source source = request.getSource();
        List<String> arguments = request.getArgumentNames();

        if (!arguments.isEmpty()) {
            throw new Error("FIXME handle args");
        }

        Reader reader = source.getReader();

        LibraryDef moduleDef = Compiler.compile(reader);

        return Truffle.getRuntime().createCallTarget(LoadLibraryNodeGen.create(this, moduleDef));
    }

    @Override
    protected boolean isVisible(PeacodContext context, Object value) {
        return !InteropLibrary.getFactory().getUncached(value).isNull(value);
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return object instanceof PeacodAny;
    }

    @Override
    protected String toString(PeacodContext context, Object value) {
        return value.toString();
    }

    @Override
    protected Object findMetaObject(PeacodContext context, Object value) {
        return ((PeacodAny) value).findMetaObject();
    }

}

