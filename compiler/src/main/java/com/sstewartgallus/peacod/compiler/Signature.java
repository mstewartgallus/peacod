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
import java.util.List;

final class Signature {
    final String libraryName;
    final String name;
    final Type type;
    final List<Type> typeParameters;

    private Signature(String libraryName, String name, Type type, List<Type> typeParameters) {
        this.libraryName = libraryName;
        this.name = name;
        this.type = type;
        this.typeParameters = typeParameters;
    }

    static Signature of(String libraryName, String name, Type type) {
        return new Signature(libraryName, name, type, Collections.emptyList());
    }

    static Signature of(String libraryName, String name, Type type, List<Type> typeParameters) {
        return new Signature(libraryName, name, type, typeParameters);
    }
}
