package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;

import java.util.Objects;
import java.util.function.Function;


@CompilerDirectives.ValueType
final class SimpleTypeLambdaTerm<A, B> extends TypeLambdaTerm<A, B> {
    private final Function<Type<A>, Term<B>> f;

    public SimpleTypeLambdaTerm(Function<Type<A>, Term<B>> f) {
        Objects.requireNonNull(f);
        this.f = f;
    }

    @Override
    public Term<B> apply(Type<A> x) {
        return this.f.apply(x);
    }

}
