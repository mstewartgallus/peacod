package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

import java.util.Arrays;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class ArgsValue extends Value<Object[]> {
    public final Object[] value;

    public ArgsValue(Object[] value) {
        this.value = value;
    }

    @Override
    public ValueNode compile() {
        throw null;
    }

    @Override
    public String toString() {
        return Arrays.toString(value);
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }
}
