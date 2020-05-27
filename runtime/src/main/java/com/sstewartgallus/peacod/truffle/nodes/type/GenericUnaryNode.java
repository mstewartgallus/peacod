package com.sstewartgallus.peacod.truffle.nodes.type;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.runtime.PeacodTypeInstance;
import com.sstewartgallus.peacod.truffle.runtime.PeacodUnaryType;

@NodeInfo(shortName = "const")
public abstract class GenericUnaryNode extends TypeNode {
    private final PeacodUnaryType generic;
    @Executed
    @Child
    TypeNode params;

    GenericUnaryNode(PeacodUnaryType generic, TypeNode params) {
        this.generic = generic;
        this.params = params;
    }

    public static GenericUnaryNode of(PeacodUnaryType generic, TypeNode params) {
        return GenericUnaryNodeGen.create(generic, params);
    }

    @ExplodeLoop
    @Specialization(guards = "param == cachedParam", limit = "1")
    final PeacodTypeInstance doGeneric(VirtualFrame frame,
                                       PeacodTypeInstance param,
                                       @Cached("param") PeacodTypeInstance cachedParam,
                                       @Cached("getCached(param)") PeacodTypeInstance result) {
        return result;
    }

    PeacodTypeInstance getCached(PeacodTypeInstance param) {
        return generic.instance(param);
    }

}
