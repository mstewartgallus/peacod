package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public abstract class IntValueNode extends ValueNode {
    private final int value;

    IntValueNode(int value) {
        this.value = value;
    }

    public static IntValueNode of(int t) {
        return IntValueNodeGen.create(t);
    }

    @Specialization
    int literal() {
        return value;
    }

    public String toString() {
        return "" + value;
    }

}
