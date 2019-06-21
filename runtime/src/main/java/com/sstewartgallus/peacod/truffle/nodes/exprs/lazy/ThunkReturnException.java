package com.sstewartgallus.peacod.truffle.nodes.exprs.lazy;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class ThunkReturnException extends ControlFlowException {
    public final Object value;

    ThunkReturnException(Object value) {
        this.value = value;
    }
}
