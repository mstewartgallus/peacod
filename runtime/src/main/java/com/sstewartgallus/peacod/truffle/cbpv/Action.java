package com.sstewartgallus.peacod.truffle.cbpv;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.sstewartgallus.peacod.truffle.PeacodTypesGen;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;

@CompilerDirectives.ValueType
public abstract class Action<A> implements TruffleObject {
    public static Value<?> apply(Action<?> action, Object[] arguments) throws ArityException {
        var ii = 0;
        for (; ; ) {
            if (action instanceof ApplyAction) {
                var apply = (ApplyAction<?, ?>) action;
                action = step(apply);
                continue;
            }
            if (action instanceof PopAction) {
                if (ii >= arguments.length) {
                    throw ArityException.create(ii + 1, arguments.length);
                }
                action = ((PopAction) action).apply(PeacodTypesGen.asValue(arguments[ii]));
                ++ii;
                continue;
            }
            if (action instanceof PushAction) {
                if (ii + 1 < arguments.length) {
                    throw ArityException.create(ii + 1, arguments.length);
                }
                var push = (PushAction<?>) action;
                return push.value;
            }
        }
    }

    private static <A, B> Action<B> step(ApplyAction<A, B> apply) {
        return ((PopAction<A, B>) apply.f).apply(apply.x);
    }

    /**
     * mostly for nailing down the semantics.
     */
    public ActionNode compile() {
        throw null;
    }
}