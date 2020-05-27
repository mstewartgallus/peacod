package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

import java.util.List;
import java.util.function.Function;

@ExportLibrary(InteropLibrary.class)
public final class IntrinsicDefinition extends Definition {
    private final String name;
    private final int numTypeArguments;
    private final int numArguments;
    private final Intrinsic intrinsic;

    private IntrinsicDefinition(String name, int numTypeArguments, int numArguments, Intrinsic intrinsic) {
        this.name = name;
        this.numTypeArguments = numTypeArguments;
        this.numArguments = numArguments;
        this.intrinsic = intrinsic;
    }

    public static IntrinsicDefinition of(String name, int numTypeParams, int numArgs, Intrinsic intrinsic) {
        return new IntrinsicDefinition(name, numTypeParams, numArgs, intrinsic);
    }

    public String toString() {
        return name;
    }

    @SuppressWarnings("static-method")
    @ExportMessage
    boolean isExecutable() {
        return true;
    }


    @Override
    public ActionNode apply(List<TypeNode> typeArguments, List<ValueNode> arguments, Function<ValueNode, ActionNode> k) {
        return intrinsic.specialize(typeArguments, arguments, k);
    }

    @Override
    public ValueNode get() {
        throw null;
    }

    public interface Intrinsic {
        ActionNode specialize(List<TypeNode> typeArguments, List<ValueNode> arguments,
                              Function<ValueNode, ActionNode> k);
    }

    @ExportMessage
    abstract static class Execute {
        @Specialization
        static Object doDirect(IntrinsicDefinition def, Object[] arguments) {
            throw new Error("unimpl");
        }
    }
}
