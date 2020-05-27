package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.value.IntValueNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class IntValue extends Value<Integer> {
    public final int value;

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public ValueNode compile() {
        return IntValueNode.of(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }
}
