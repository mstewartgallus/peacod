package com.sstewartgallus.peacod.truffle.nodes.exprs.lazy;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class ThunkIntReturnException extends ControlFlowException {
    public final int value;

    ThunkIntReturnException(int value) {
        this.value = value;
    }
}
