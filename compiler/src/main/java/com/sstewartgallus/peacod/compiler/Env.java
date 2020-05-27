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
package com.sstewartgallus.peacod.compiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

// fixme.. use explicit scopings...
final class Env {
    private static final Env EMPTY = new Env(null, Collections.emptyMap());

    private final Env predecessor;
    private final Map<Var<?>, Object> map;

    private Env(Env predecessor, Map<Var<?>, Object> map) {
        this.predecessor = predecessor;
        this.map = Objects.requireNonNull(map);
    }

    static Env empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    Env scope() {
        return new Env(this, new HashMap<>());
    }

    <T> Env put(Var<T> domain, T value) {
        Map<Var<?>, Object> copy = new HashMap<>(map);
        copy.put(domain, value);
        return new Env(predecessor, copy);
    }


    <T> T get(Var<T> var) {
        var value = (T) map.get(var);
        if (null == value && predecessor != null) {
            return predecessor.get(var);
        }
        return value;
    }

    <T> Env transform(Var<T> var, Function<T, T> f) {
        Map<Var<?>, Object> copy = new HashMap<>(map);
        copy.put(var, f.apply(get(var)));
        return new Env(predecessor, copy);
    }

    static final class Var<T> {
        private final String name;

        Var(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
