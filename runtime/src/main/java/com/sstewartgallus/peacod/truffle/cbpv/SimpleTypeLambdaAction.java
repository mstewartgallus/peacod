package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.plato.Type;

import java.util.Objects;
import java.util.function.Function;


@CompilerDirectives.ValueType
final class SimpleTypeLambdaAction<A, B> extends TypeLambdaAction<A, B> {
    private final Function<Type<A>, Value<B>> f;

    public SimpleTypeLambdaAction(Function<Type<A>, Value<B>> f) {
        this.f = Objects.requireNonNull(f);
    }

    @Override
    public Value<B> apply(Type<A> x) {
        return this.f.apply(x);
    }

}
