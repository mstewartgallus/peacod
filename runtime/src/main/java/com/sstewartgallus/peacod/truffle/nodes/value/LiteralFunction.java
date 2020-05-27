package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.runtime.Fn;

import java.util.Objects;

@NodeInfo(shortName = "const")
public abstract class LiteralFunction extends ValueNode {
    private final Fn value;

    LiteralFunction(Fn value) {
        this.value = Objects.requireNonNull(value);
    }

    public static LiteralFunction of(Fn value) {
        return LiteralFunctionNodeGen.create(value);
    }

    @Specialization
    Fn literal() {
        return value;
    }

    public String toString() {
        return value.toString();
    }

}
