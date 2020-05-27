package com.sstewartgallus.peacod.truffle.plato;


import com.oracle.truffle.api.CompilerDirectives;

@CompilerDirectives.ValueType
public abstract class TypeLambdaTerm<A, B> extends Term<V<A, B>> {
    public abstract Term<B> apply(Type<A> x);

}
