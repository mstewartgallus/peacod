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
package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.nodes.type.GenericUnaryNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PeacodUnaryType extends PeacodType {
    private final Map<PeacodTypeInstance, PeacodTypeInstance> cache;

    PeacodUnaryType() {
        this.cache = new HashMap<>();
    }

    @Override
    public Object findMetaObject() {
        return "type";
    }

    public TypeNode factory(TypeNode... args) {
        if (args.length != 1) {
            throw new Error("wrong nonunary arityy of args " + args);
        }
        return GenericUnaryNode.of(this, args[0]);
    }

    // fixme.. make concurrent safe?
    @CompilerDirectives.TruffleBoundary
    @Override
    public PeacodTypeInstance instance(PeacodTypeInstance... args) {
        if (args.length != 1) {
            throw new Error("wrong nonunary arityy of args " + args);
        }
        return cache.computeIfAbsent(args[0], k -> PeacodTypeInstance.of(this, Collections.singletonList(k)));
    }
}
