package com.sstewartgallus.peacod.truffle.cbpv;

import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.ForceActionNode;
import com.sstewartgallus.peacod.truffle.plato.U;

import java.util.Objects;

public final class ForceAction<A> extends Action<A> {
    private final Value<U<A>> term;

    public ForceAction(Value<U<A>> term) {
        this.term = Objects.requireNonNull(term);
    }

    @Override
    public ActionNode compile() {
        return ForceActionNode.of(term.compile());
    }
}
