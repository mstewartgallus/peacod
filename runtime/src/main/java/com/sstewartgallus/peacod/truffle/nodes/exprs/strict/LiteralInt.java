package com.sstewartgallus.peacod.truffle.nodes.exprs.strict;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;

@NodeInfo(shortName = "const")
public final class LiteralInt extends StrictNode {
    private final int value;

    private LiteralInt(int value) {
        this.value = value;
    }

    public static LiteralInt of(int t) {
        return new LiteralInt(t);
    }

    @Override
    public int executeInteger(VirtualFrame frame) {
        return value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return value;
    }
}
