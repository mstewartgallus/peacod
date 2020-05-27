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
import java.util.Objects;

final class Signature {
    final String libraryName;
    final String name;
    final TypeScheme typeScheme;

    private Signature(String libraryName, String name, TypeScheme scheme) {
        this.libraryName = Objects.requireNonNull(libraryName);
        this.name = Objects.requireNonNull(name);
        this.typeScheme = Objects.requireNonNull(scheme);
    }

    static Signature of(String libraryName, String name, Type type) {
        return Signature.of(libraryName, name, TypeScheme.over(Collections.emptyList(), type));
    }

    static Signature of(String libraryName, String name, TypeScheme scheme) {
        Objects.requireNonNull(libraryName);
        return new Signature(libraryName, name, scheme);
    }

    public String toString() {
        return libraryName + "." + name + ":" + typeScheme;
    }
}
