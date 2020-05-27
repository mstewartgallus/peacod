package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.TruffleObject;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;
import com.sstewartgallus.peacod.truffle.plato.Type;

@CompilerDirectives.ValueType
public abstract class Value<A> implements TruffleObject {
    /**
     * mostly for nailing down the semantics.
     */
    public ValueNode compile() {
        throw null;
    }

    public Type<A> type() {
        throw null;
    }
}