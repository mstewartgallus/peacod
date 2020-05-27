package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.TruffleObject;

import java.util.Objects;

@CompilerDirectives.ValueType
public final class Fn implements TruffleObject {
    public final PeacodTypeInstance type;
    public final RootCallTarget target;

    public Fn(PeacodTypeInstance type, RootCallTarget target) {
        this.type = type;
        this.target = Objects.requireNonNull(target);
    }

    public boolean equals(Object obj) {
        return obj instanceof Fn && ((Fn) obj).target == target;
    }

    public String toString() {
        return target.getRootNode().toString();
    }
}
