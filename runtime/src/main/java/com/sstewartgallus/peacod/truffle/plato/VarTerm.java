package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.runtime.Id;

import java.util.Objects;


@CompilerDirectives.ValueType
public final class VarTerm<A> extends Term<A> {
    public final Type<A> range;
    private final Id id;

    public VarTerm(Type<A> domain, Id id) {
        this.range = Objects.requireNonNull(domain);
        this.id = Objects.requireNonNull(id);
    }

}
