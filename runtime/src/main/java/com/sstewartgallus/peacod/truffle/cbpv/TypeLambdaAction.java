package com.sstewartgallus.peacod.truffle.cbpv;


import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.plato.V;

@CompilerDirectives.ValueType
public abstract class TypeLambdaAction<A, B> extends Action<V<A, B>> {
    public abstract Value<B> apply(Type<A> x);

}
