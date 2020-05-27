package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;

@ImportStatic(NodeUtils.class)
@NodeInfo(shortName = "load")
public abstract class ArgumentNode extends ValueNode {
    private final int source;

    // fixme... use type info... ?
    ArgumentNode(TypeNode type, int source) {
        this.source = source;
    }

    public static ArgumentNode of(TypeNode type, int index) {
        return ArgumentNodeGen.create(type, index);
    }

    @Specialization
    Object load(VirtualFrame frame) {
        return frame.getArguments()[source];
    }

    public String toString() {
        return "V@" + source;
    }
}
