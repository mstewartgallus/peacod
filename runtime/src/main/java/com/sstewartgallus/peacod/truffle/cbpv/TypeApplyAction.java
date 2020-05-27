package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.plato.V;

import java.util.Objects;

@CompilerDirectives.ValueType
public final class TypeApplyAction<A, B> extends Action<B> {
    private final Action<V<A, B>> f;
    private final Type<A> x;

    public TypeApplyAction(Action<V<A, B>> f, Type<A> x) {
        this.f = Objects.requireNonNull(f);
        this.x = Objects.requireNonNull(x);
    }

}
