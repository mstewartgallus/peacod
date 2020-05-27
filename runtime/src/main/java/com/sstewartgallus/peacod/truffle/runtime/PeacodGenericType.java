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
import com.sstewartgallus.peacod.truffle.nodes.type.GenericNode;
import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PeacodGenericType extends PeacodType {

    private final int arity;
    private final Map<List<PeacodTypeInstance>, PeacodTypeInstance> cache;

    PeacodGenericType(int arity) {
        if (arity < 0) {
            throw new Error("arity " + arity + " < 1");
        }
        this.arity = arity;
        this.cache = new HashMap<>();
    }

    public static PeacodGenericType of(int arity) {
        return new PeacodGenericType(arity);
    }

    @Override
    public Object findMetaObject() {
        return "type";
    }


    @Override
    public TypeNode factory(TypeNode... args) {
        if (args.length != arity) {
            throw new Error(Arrays.toString(args) + " not size " + arity);
        }
        return GenericNode.of(this, args);
    }

    // fixme.. make concurrent safe?
    @Override
    @CompilerDirectives.TruffleBoundary
    public PeacodTypeInstance instance(PeacodTypeInstance... args) {
        return cache.computeIfAbsent(Arrays.asList(args), k -> PeacodTypeInstance.of(this, k));
    }
}
