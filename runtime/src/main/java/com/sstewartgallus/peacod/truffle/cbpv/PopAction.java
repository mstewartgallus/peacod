package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.plato.Fun;
import com.sstewartgallus.peacod.truffle.plato.Type;

import java.util.Objects;

@CompilerDirectives.ValueType
public abstract class PopAction<A, B> extends Action<Fun<A, B>> {
    public final Type<A> domain;

    public PopAction(Type<A> domain) {
        Objects.requireNonNull(domain);
        this.domain = domain;
    }

    @Override
    public ActionNode compile() {
        // fixme... point free?
        throw null;
    }

    public abstract Action<B> apply(Value<A> value);
}
