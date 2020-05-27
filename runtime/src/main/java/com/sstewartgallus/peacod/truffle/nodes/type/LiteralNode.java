package com.sstewartgallus.peacod.truffle.nodes.type;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.PeacodLanguage;
import com.sstewartgallus.peacod.truffle.runtime.PeacodTypeInstance;

import java.util.Arrays;
import java.util.List;

@NodeInfo(shortName = "const")
public abstract class LiteralNode extends TypeNode {
    public static LiteralNode of(String name, List<TypeNode> params) {
        return new Uninit(name, params);
    }

    private static final class Uninit extends LiteralNode {
        @Children
        private final TypeNode[] params;
        private final String name;

        private Uninit(String name, List<TypeNode> params) {
            this.name = name;
            this.params = params.toArray(new TypeNode[0]);
        }

        @Override
        @ExplodeLoop
        public PeacodTypeInstance execute(VirtualFrame frame) {
            CompilerDirectives.transferToInterpreterAndInvalidate();

            // fixme.. make concurrent safe!

            // FIXME... use lookupContextReference instead?
            var type = PeacodLanguage.getContext().lookup(name);

            var replacement = type.factory(params);
            replace(replacement);
            return replacement.execute(frame);
        }

        public String toString() {
            return "(" + name + Arrays.toString(params) + ")";
        }
    }
}
