package com.sstewartgallus.peacod.truffle.nodes.type;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.runtime.PeacodGenericType;
import com.sstewartgallus.peacod.truffle.runtime.PeacodTypeInstance;

@NodeInfo(shortName = "const")
public final class GenericNode extends TypeNode {
    private final PeacodGenericType generic;
    @Children
    private final TypeNode[] params;

    private GenericNode(PeacodGenericType generic, TypeNode[] params) {
        this.generic = generic;
        this.params = params;
    }

    public static GenericNode of(PeacodGenericType generic, TypeNode[] params) {
        return new GenericNode(generic, params);
    }

    @Override
    @ExplodeLoop
    public final PeacodTypeInstance execute(VirtualFrame frame) {
        var args = new PeacodTypeInstance[params.length];
        for (var ii = 0; ii < params.length; ++ii) {
            var argument = params[ii].execute(frame);
            args[ii] = argument;
        }
        return generic.instance(args);
    }
}
