package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "const")
public abstract class BooleanValueNode extends ValueNode {
    private final boolean value;

    BooleanValueNode(boolean value) {
        this.value = value;
    }

    public static BooleanValueNode of(boolean t) {
        return BooleanValueNodeGen.create(t);
    }

    @Specialization
    boolean literal() {
        return value;
    }

}
