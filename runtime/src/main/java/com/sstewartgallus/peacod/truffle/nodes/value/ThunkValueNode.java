package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.PeacodTypesGen;
import com.sstewartgallus.peacod.truffle.cbpv.*;
import com.sstewartgallus.peacod.truffle.nodes.PeacodThunkRootNode;
import com.sstewartgallus.peacod.truffle.nodes.action.ActionNode;
import com.sstewartgallus.peacod.truffle.plato.F;
import com.sstewartgallus.peacod.truffle.plato.Type;

import java.util.Objects;

@NodeInfo(shortName = "delay")
public abstract class ThunkValueNode extends ValueNode {
    private final RootCallTarget target;

    ThunkValueNode(RootCallTarget target) {
        this.target = target;
    }

    public static ThunkValueNode of(ActionNode action) {
        var root = new PeacodThunkRootNode(action);
        var target = Truffle.getRuntime().createCallTarget(root);
        return ThunkValueNodeGen.create(target);
    }

    @Specialization
    Value<?> delay(VirtualFrame frame) {
        var lambda = new CallTargetWrapper(target);
        return new ThunkValue<>(new ApplyAction<>(lambda, new ArgsValue(frame.getArguments())));
    }

    public String toString() {
        return "(thunk " + target.getRootNode().toString() + ")";
    }
}

@CompilerDirectives.ValueType
final class CallTargetWrapper extends PopAction<Object[], F<Object>> {
    private final RootCallTarget target;

    public CallTargetWrapper(RootCallTarget target) {
        super(Type.ARRAY_TYPE);
        this.target = Objects.requireNonNull(target);
    }

    @Override
    public Action<F<Object>> apply(Value<Object[]> value) {
        var args = ((ArgsValue) value).value;
        var result = target.call(args);
        return (Action<F<Object>>) PeacodTypesGen.asAction(result);
    }

    @Override
    public String toString() {
        return target.getRootNode().toString();
    }
};
