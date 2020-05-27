package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;

import java.util.Objects;

@CompilerDirectives.ValueType
public final class TypeApplyTerm<A, B> extends Term<B> {
    private final Term<V<A, B>> f;
    private final Type<A> x;

    public TypeApplyTerm(Term<V<A, B>> f, Type<A> x) {
        this.f = Objects.requireNonNull(f);
        this.x = Objects.requireNonNull(x);
    }

}
