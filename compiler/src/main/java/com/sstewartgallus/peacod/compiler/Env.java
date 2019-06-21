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
import java.util.function.Function;

final class Env {
    private static final Env EMPTY = new Env(Collections.emptyMap());
    private final Map<Var<?>, Object> map;

    private Env(Map<Var<?>, Object> map) {
        this.map = map;
    }

    static Env empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    <T> Env put(Var<T> domain, T value) {
        Map<Var<?>, Object> copy = new HashMap<>(map);
        copy.put(domain, value);
        return new Env(copy);
    }


    <T> T get(Var<T> var) {
        return (T) map.get(var);
    }

    <T> Env transform(Var<T> var, Function<T, T> f) {
        Map<Var<?>, Object> copy = new HashMap<>(map);
        copy.put(var, f.apply((T) map.get(var)));
        return new Env(copy);
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
