package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.runtime.Id;

import java.util.Objects;
import java.util.function.Function;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class SimplePopAction<A, B> extends PopAction<A, B> {
    private final Function<Value<A>, Action<B>> f;

    public SimplePopAction(Type<A> domain,
                           Function<Value<A>, Action<B>> f) {
        super(domain);
        this.f = Objects.requireNonNull(f);
    }

    @Override
    public String toString() {
        var id = new Id();
        return "(\\" + id + " -> " + f.apply(new VarValue<>(domain, id)) + ")";
    }

    @ExportMessage
    @CompilerDirectives.TruffleBoundary
    String toDisplayString(boolean allowSideEffects) {
        return toString();
    }

    @Override
    public Action<B> apply(Value<A> value) {
        return f.apply(value);
    }
}
