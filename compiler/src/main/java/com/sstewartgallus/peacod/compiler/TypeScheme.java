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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

final class TypeScheme {
    final List<Type.Hole> holes;
    final Type type;

    private TypeScheme(List<Type.Hole> holes, Type type) {
        this.holes = Objects.requireNonNull(holes);
        this.type = Objects.requireNonNull(type);
    }

    static TypeScheme over(List<Type.Hole> holes, Type type) {
        return new TypeScheme(holes, type);
    }

    @Override
    public String toString() {
        if (holes.isEmpty()) {
            return type.toString();
        }
        return "∀" + holes + ". " + type;
    }

    TypeScheme copy() {
        Map<Type, Type> typeMap = holes.stream().collect(Collectors.toMap(e -> e, e -> new Type.Hole()));
        return TypeScheme.over(
                holes
                        .stream()
                        .map(e -> (Type.Hole) Constraints.resolve(typeMap, e))
                        .collect(Collectors.toList()),
                Constraints.resolve(typeMap, type));
    }

}
