/*
 * Copyright 2019 Steven Stewart-Gallus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sstewartgallus.peacod.truffle.nodes.type;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;
import com.sstewartgallus.peacod.truffle.nodes.BaseNode;
import com.sstewartgallus.peacod.truffle.runtime.PeacodType;

import java.io.File;
import java.util.List;

@NodeInfo(description = "The abstract base nodes for all type expressions")
public abstract class TypeNode extends BaseNode {

    private static TypeNode of(PeacodType t) {
        return new Literal(t);
    }

    public static TypeNode ofLiteral(String name, List<TypeNode> params) {
        // fixme cleanup...
        switch (name) {
            default:
                throw new Error("unimpl " + name);

            case "boolean":
                return TypeNode.of(PeacodType.BOOLEAN_VALUE);
            case "byte":
                return TypeNode.of(PeacodType.BYTE_VALUE);
            case "short":
                return TypeNode.of(PeacodType.SHORT_VALUE);
            case "int":
                return TypeNode.of(PeacodType.INT_VALUE);
            case "long":
                return TypeNode.of(PeacodType.LONG_VALUE);

            case "fn": {
                // FIXME make generic over templates
                int size = params.size();
                TypeNode result = params.get(size - 1);
                List<TypeNode> arguments = params.subList(0, size - 1);
                return FunctionNode.of(result, arguments);
            }
        }
    }

    public abstract PeacodType execute(VirtualFrame frame);

    @NodeInfo(shortName = "const")
    private static final class Literal extends TypeNode {
        private final PeacodType value;

        private Literal(PeacodType value) {
            this.value = value;
        }

        @Override
        public PeacodType execute(VirtualFrame frame) {
            return value;
        }
    }
}
