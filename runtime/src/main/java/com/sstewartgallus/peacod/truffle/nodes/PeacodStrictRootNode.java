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
package com.sstewartgallus.peacod.truffle.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.sstewartgallus.peacod.truffle.nodes.exprs.strict.StrictNode;

public final class PeacodStrictRootNode extends PeacodRootNode {
    @Child
    private StrictNode body;

    public PeacodStrictRootNode(String name, StrictNode body) {
        super(name);
        this.body = body;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return body.execute(frame);
    }

    public final boolean strict() {
        return true;
    }
}
