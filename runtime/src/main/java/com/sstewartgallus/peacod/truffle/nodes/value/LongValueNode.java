package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public abstract class LongValueNode extends ValueNode {
    private final long value;

    LongValueNode(long value) {
        this.value = value;
    }

    public static LongValueNode of(long value) {
        return LongValueNodeGen.create(value);
    }

    @Specialization
    long literal() {
        return value;
    }

}
