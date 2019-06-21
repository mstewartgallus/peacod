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
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.sstewartgallus.peacod.truffle.runtime.Function;
import com.sstewartgallus.peacod.truffle.runtime.PeacodType;

import java.util.List;

// FIXME: Make generic to any type constructor
// FIXME: Make constant
public final class FunctionNode extends TypeNode {
    @Children
    private final TypeNode[] arguments;
    @Child
    private TypeNode result;

    private FunctionNode(TypeNode result, List<TypeNode> arguments) {
        this.result = result;
        this.arguments = arguments.toArray(new TypeNode[0]);
    }

    public static FunctionNode of(TypeNode result, List<TypeNode> arguments) {
        return new FunctionNode(result, arguments);
    }

    @Override
    @ExplodeLoop
    public PeacodType execute(VirtualFrame frame) {
        int ii = 0;
        PeacodType[] args = new PeacodType[arguments.length];
        for (TypeNode argument : arguments) {
            int arg = ii++;
            args[arg] = argument.execute(frame);
        }

        PeacodType resultType = result.execute(frame);

        return new Function(args, resultType);
    }
}
