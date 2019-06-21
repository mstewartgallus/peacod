package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@CompilerDirectives.ValueType
@ExportLibrary(InteropLibrary.class)
public final class Definition implements TruffleObject {
    public final RootCallTarget produceProcedure;

    private Definition(RootCallTarget produceProcedure) {
        this.produceProcedure = produceProcedure;
    }

    public static Definition of(RootCallTarget produceProcedure) {
        return new Definition(produceProcedure);
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ExportMessage
    abstract static class Execute {
        @com.oracle.truffle.api.dsl.Specialization
        protected static Spec doDirect(Definition def, Object[] arguments) {
            return (Spec) def.produceProcedure.call(arguments);
        }
    }
}
