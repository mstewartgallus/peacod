package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.ApplyActionNode;
import com.sstewartgallus.peacod.truffle.plato.Fun;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class ApplyAction<A, B> extends Action<B> {
    public final Action<Fun<A, B>> f;
    public final Value<A> x;

    public ApplyAction(Action<Fun<A, B>> f, Value<A> x) {
        this.f = Objects.requireNonNull(f);
        this.x = Objects.requireNonNull(x);
    }

    public Action<B> step() {
        // fixme... use generic application protocol?
        return ((PopAction<A, B>) f).apply(x);
    }

    @Override
    public ActionNode compile() {
        return ApplyActionNode.of(f.compile(), x.compile());
    }

    @Override
    public String toString() {
        return "(" + f + " " + x + ")";
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }
}