package com.sstewartgallus.peacod.truffle.nodes.exprs.strict;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.nodes.exprs.PolyNode;

@NodeInfo(shortName = "const")
public final class LiteralLong extends StrictNode {
    private final long value;

    LiteralLong(long value) {
        this.value = value;
    }

    public static LiteralLong of(long t) {
        return new LiteralLong(t);
    }

    @Override
    public long executeLong(VirtualFrame frame) {
        return value;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return value;
    }
}
