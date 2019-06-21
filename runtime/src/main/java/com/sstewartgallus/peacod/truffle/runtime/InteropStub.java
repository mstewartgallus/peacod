package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.LazyNode;

final class InteropStub extends LazyNode {
    private final RootCallTarget target;

    InteropStub(RootCallTarget target) {
        this.target = target;
    }

    @Override
    public Thunk execute(VirtualFrame frame) {
        return Thunk.of(target, frame.getArguments());
    }
}
