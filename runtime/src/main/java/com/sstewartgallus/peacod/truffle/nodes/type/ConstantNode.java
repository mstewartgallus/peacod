package com.sstewartgallus.peacod.truffle.nodes.type;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.runtime.PeacodTypeInstance;

@NodeInfo(shortName = "const")
public final class ConstantNode extends TypeNode {
    private final PeacodTypeInstance constantInstance;

    private ConstantNode(PeacodTypeInstance constantInstance) {
        this.constantInstance = constantInstance;
    }

    public static TypeNode of(PeacodTypeInstance constantInstance) {
        return new ConstantNode(constantInstance);
    }

    @Override
    public PeacodTypeInstance execute(VirtualFrame frame) {
        return constantInstance;
    }
}
