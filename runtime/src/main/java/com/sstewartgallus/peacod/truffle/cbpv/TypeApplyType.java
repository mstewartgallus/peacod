package com.sstewartgallus.peacod.truffle.cbpv;

import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.plato.V;

import java.util.Objects;

public final class TypeApplyType<A, B> implements Type<B> {
    private final Type<V<A, B>> f;
    private final Type<A> x;

    public TypeApplyType(Type<V<A, B>> f, Type<A> x) {
        this.f = Objects.requireNonNull(f);
        this.x = Objects.requireNonNull(x);
    }

}