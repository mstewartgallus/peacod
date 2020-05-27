package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.cbpv.ApplyAction;
import com.sstewartgallus.peacod.truffle.cbpv.ThunkValue;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class ApplyTerm<A, B> extends Term<B> {
    public final Term<Fun<U<A>, B>> f;
    public final Term<A> x;

    public ApplyTerm(Term<Fun<U<A>, B>> f, Term<A> x) {
        this.f = Objects.requireNonNull(f);
        this.x = Objects.requireNonNull(x);
    }

    @Override
    public Action<B> compile() {
        return new ApplyAction<>(f.compile(), new ThunkValue<>(x.compile()));
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
