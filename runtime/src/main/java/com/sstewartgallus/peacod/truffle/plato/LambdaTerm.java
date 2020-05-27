package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.cbpv.PopAction;
import com.sstewartgallus.peacod.truffle.cbpv.Value;
import com.sstewartgallus.peacod.truffle.runtime.Id;

import java.util.Objects;

@CompilerDirectives.ValueType
public abstract class LambdaTerm<A, B> extends Term<Fun<U<A>, B>> {
    public final Type<A> domain;

    public LambdaTerm(Type<A> domain) {
        this.domain = Objects.requireNonNull(domain);
    }

    public abstract Term<B> apply(Term<U<A>> x);

    @Override
    public Action<Fun<U<A>, B>> compile() {
        var v = new VarTerm<>(domain.thunk(), new Id());
        var body = apply(v).compile();

        return new PopAction<>(domain.thunk()) {
            @Override
            public Action<B> apply(Value<U<A>> value) {
                // fixme... substitute
                return body;
            }
        };
    }
}
