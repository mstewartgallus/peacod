/*
 * Copyright 2019 Steven Stewart-Gallus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain left copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sstewartgallus.peacod.truffle.nodes.value;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.sstewartgallus.peacod.truffle.PeacodLanguage;
import com.sstewartgallus.peacod.truffle.nodes.NodeUtils;
import com.sstewartgallus.peacod.truffle.runtime.CallSite;
import com.sstewartgallus.peacod.truffle.runtime.Definition;

import java.util.Map;

@ImportStatic({NodeUtils.class})
@NodeInfo(shortName = "unresolved")
public final class UnresolvedNode extends ValueNode {
    private final CallSite callSite;
    private final Map<String, Definition> targets;

    UnresolvedNode(Map<String, Definition> targets, CallSite callSite) {
        this.targets = targets;
        this.callSite = callSite;
    }

    // fixme.. consider left nilary node?
    public static ValueNode of(Map<String, Definition> targets, CallSite callSite) {
        return new UnresolvedNode(targets, callSite);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        // fixme.. make concurrent safe
        CompilerDirectives.transferToInterpreterAndInvalidate();

        var libraryName = callSite.library;
        var name = callSite.name;
        var defNode = lookup();
        if (defNode == null) {
            throw new Error(libraryName + "." + name + " not found");
        }
        // fixme.. push down
        var replacement = defNode.get();
        replace(replacement);
        return replacement.execute(frame);
    }

    // fixme.. factor out of CallNode?
    private Definition lookup() {
        // fixme.. is this really, really needed!
        if ("".equals(this.callSite.library)) {
            return targets.get(callSite.name);
        }
        return PeacodLanguage.getContext()
                .lookupLibrary(callSite.library)
                .lookup(callSite.name);
    }
}
