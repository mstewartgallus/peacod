package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;
import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.runtime.Id;

import java.util.Objects;

@CompilerDirectives.ValueType
public final class VarValue<A> extends Value<A> {
    public final Type<A> range;
    private final Id id;

    public VarValue(Type<A> domain, Id id) {
        this.range = Objects.requireNonNull(domain);
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public ValueNode compile() {
        throw null;
    }
}
