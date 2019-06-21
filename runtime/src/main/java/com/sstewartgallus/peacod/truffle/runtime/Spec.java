package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.PeacodLazyRootNode;
import com.sstewartgallus.peacod.truffle.nodes.PeacodStrictRootNode;
import com.sstewartgallus.peacod.truffle.nodes.exprs.lazy.LazyNode;

@ExportLibrary(InteropLibrary.class)
@CompilerDirectives.ValueType
public final class Spec implements TruffleObject {
    public final RootCallTarget target;

    private Spec(RootCallTarget target) {
        this.target = target;
    }

    public static Spec of(RootCallTarget target) {
        return new Spec(target);
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @ExportMessage
    abstract static class Execute {
        @com.oracle.truffle.api.dsl.Specialization
        static Object doDirect(Spec procedure, Object[] arguments) {
            return interop(procedure).call(arguments);
        }

        static RootCallTarget interop(Spec specialization) {
            RootCallTarget target = specialization.target;
            if (target.getRootNode() instanceof PeacodLazyRootNode) {
                return Truffle.getRuntime().createCallTarget(new PeacodStrictRootNode(null, new InteropStub(target).force()));
            } else {
                return target;
            }
        }
    }

}

