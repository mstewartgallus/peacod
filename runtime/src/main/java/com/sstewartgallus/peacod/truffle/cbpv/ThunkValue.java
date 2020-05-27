package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.value.ThunkValueNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;
import com.sstewartgallus.peacod.truffle.plato.U;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class ThunkValue<A> extends Value<U<A>> {
    public final Action<A> action;

    public ThunkValue(Action<A> action) {
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public ValueNode compile() {
        return ThunkValueNode.of(action.compile());
    }

    @Override
    public String toString() {
        return "(thunk " + action + ")";
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @ExportMessage
    public boolean isExecutable() {
        return true;
    }

    @ExportMessage
    Value<?> execute(Object[] arguments) throws ArityException {
        if (arguments.length != 0) {
            throw ArityException.create(0, arguments.length);
        }
        return Action.apply((Action) action, arguments);
    }
}
