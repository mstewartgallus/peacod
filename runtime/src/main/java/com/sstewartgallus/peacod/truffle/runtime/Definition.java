package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.TruffleObject;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.nodes.type.PeacodTypeRootNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ValueNode;

import java.util.List;
import java.util.function.Function;

public abstract class Definition implements TruffleObject {
    public static Definition of(String name, int numTypeParams, int numArgs, TypeNode typeNode, ActionNode bodyNode) {
        var typeTarget = Truffle.getRuntime().createCallTarget(new PeacodTypeRootNode(name, typeNode));
        if (numTypeParams == 0) {
            return NongenericDefinition.of(name, numArgs, (PeacodTypeInstance) typeTarget.call(), bodyNode);
        }
        throw new Error("generics are unimplemented");
    }

    public abstract ActionNode apply(List<TypeNode> typeArguments, List<ValueNode> arguments, Function<ValueNode, ActionNode> k);

    public abstract ValueNode get();
}
