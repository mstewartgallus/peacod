package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.sstewartgallus.peacod.truffle.cbpv.Action;
import com.sstewartgallus.peacod.truffle.nodes.PeacodNongenericRootNode;
import com.sstewartgallus.peacod.truffle.nodes.PeacodRootNode;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.action.CallNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ThunkValueNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

import java.util.List;
import java.util.function.Function;

@ExportLibrary(InteropLibrary.class)
public final class NongenericDefinition extends Definition {

    private final PeacodTypeInstance type;
    private final int arity;
    // fixme... dynamically create?
    private final RootCallTarget target;

    private NongenericDefinition(PeacodTypeInstance type, int numArguments, RootCallTarget target) {
        this.type = type;
        this.arity = numArguments;
        this.target = target;
    }

    public static NongenericDefinition of(String name, int numArgs, PeacodTypeInstance type, ActionNode bodyNode) {
        PeacodRootNode thunker = new PeacodNongenericRootNode(name, numArgs, type, bodyNode);
        var target = Truffle.getRuntime().createCallTarget(thunker);
        return new NongenericDefinition(type, numArgs, target);
    }

    private PeacodRootNode rootNode() {
        return (PeacodRootNode) target.getRootNode();
    }

    public String toString() {
        return rootNode().graph();
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isExecutable() {
        return true;
    }

    @Override
    public ActionNode apply(List<TypeNode> typeArguments, List<ValueNode> arguments, Function<ValueNode, ActionNode> k) {
        // fixme.. handle saturated calls/supersaturated/undersaturated differently..
        if (!typeArguments.isEmpty()) {
            throw null;
        }
        if (arguments.size() != arity) {
            throw null;
        }

        var callNode = DirectCallNode.create(target);
        return k.apply(ThunkValueNode.of(CallNode.of(callNode, arguments)));
    }

    @Override
    public ValueNode get() {
        throw null;
    }

    @ExportMessage
    abstract static class Execute {
        @Specialization
        static Object doDirect(NongenericDefinition def, Object[] arguments,
                               @Cached IndirectCallNode callNode) throws ArityException {
            // fixme... handle partial applications....
            var value = callNode.call(def.target, arguments);
            if (value instanceof Action) {
                return Action.apply((Action) value, arguments);
            }
            return value;
        }

    }
}
