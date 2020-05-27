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
import java.util.Objects;

final class Definition {
    final TypeScheme scheme;
    final List<Type> arguments;
    final Term expr;

    private Definition(TypeScheme scheme, List<Type> arguments, Term expr) {
        this.scheme = Objects.requireNonNull(scheme);
        this.arguments = Objects.requireNonNull(arguments);
        this.expr = Objects.requireNonNull(expr);
    }

    // fixme.. push down arguments?
    static Definition of(LetBindings letEnv, TypeScheme scheme, List<Type> arguments, Term expr) {
        return new Definition(scheme, arguments, expr);
    }

    @Override
    public String toString() {
        return expr + " : " + scheme;
    }

}
