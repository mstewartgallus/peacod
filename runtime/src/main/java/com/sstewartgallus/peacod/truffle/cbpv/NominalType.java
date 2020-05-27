package com.sstewartgallus.peacod.truffle.cbpv;

import com.sstewartgallus.peacod.truffle.plato.Type;
import com.sstewartgallus.peacod.truffle.plato.TypeTag;

public final class NominalType<A> implements Type<A> {
    private final TypeTag<A> tag;

    // fixme... cache... in hashmap?
    private NominalType(TypeTag<A> tag) {
        this.tag = tag;
    }

    public static <A> NominalType<A> ofTag(TypeTag<A> tag) {
        return new NominalType<>(tag);
    }

    public TypeTag<A> tag() {
        return tag;
    }

    @Override
    public String toString() {
        return tag.toString();
    }
}