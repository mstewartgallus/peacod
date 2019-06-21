package com.sstewartgallus.peacod.truffle.nodes.exprs.strict;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;

@NodeInfo(shortName = "const")
public final class LiteralBoolean extends StrictNode {
    private final boolean value;

    private LiteralBoolean(boolean value) {
        this.value = value;
    }

    public static LiteralBoolean of(boolean t) {
        return new LiteralBoolean(t);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) {
        return value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return value;
    }
}
