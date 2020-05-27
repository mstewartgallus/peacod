package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.PushActionNode;
import com.sstewartgallus.peacod.truffle.plato.F;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class PushAction<A> extends Action<F<A>> {
    public final Value<A> value;

    public PushAction(Value<A> term) {
        this.value = Objects.requireNonNull(term);
    }

    @Override
    public ActionNode compile() {
        return PushActionNode.of(value.compile());
    }

    @Override
    public String toString() {
        return "(push " + value + ")";
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

}
