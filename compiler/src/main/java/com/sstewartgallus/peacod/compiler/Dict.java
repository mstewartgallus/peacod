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
import java.util.Set;

final class Dict<T> {
    private final Map<String, T> map;

    Dict() {
        this.map = Collections.emptyMap();
    }

    private Dict(Map<String, T> map) {
        this.map = map;
    }

    T get(String name) {
        return map.get(name);
    }

    Dict<T> put(String name, T value) {
        Map<String, T> copy = new HashMap<>(map);
        copy.put(name, value);
        return new Dict<>(copy);
    }

    Set<Map.Entry<String, T>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
