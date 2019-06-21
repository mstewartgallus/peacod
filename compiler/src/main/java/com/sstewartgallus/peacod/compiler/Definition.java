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

final class Definition {
    final List<Type> typeArguments;
    final List<Type> arguments;
    final Type type;
    final Term expr;

    private Definition(List<Type> typeArguments, List<Type> arguments, Type type, Term expr) {
        this.typeArguments = typeArguments;
        this.arguments = arguments;
        this.type = type;
        this.expr = expr;
    }

    static Definition of(List<Type> typeArguments, List<Type> arguments, Type type, Term expr) {
        return new Definition(typeArguments, arguments, type, expr);
    }

    @Override
    public String toString() {
        return expr + " : " + type;
    }

}
