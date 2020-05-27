package com.sstewartgallus.peacod.truffle.plato;

import java.util.function.Function;

public final class ForallType<A, B> implements Type<V<A, B>> {
    private final Function<Type<A>, Type<B>> f;

    public ForallType(Function<Type<A>, Type<B>> f) {
        this.f = f;
    }

    public Function<Type<A>, Type<B>> f() {
        return f;
    }
}
