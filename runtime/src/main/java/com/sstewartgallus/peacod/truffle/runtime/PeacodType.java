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

import com.sstewartgallus.peacod.truffle.nodes.type.TypeNode;

public abstract class PeacodType extends PeacodObject {
    public static PeacodType of(int arity) {
        switch (arity) {
            case 0:
                return new PeacodConstantType();
            case 1:
                return new PeacodUnaryType();
            default:
                return new PeacodGenericType(arity);
        }
    }

    @Override
    public Object findMetaObject() {
        return "type";
    }

    public abstract PeacodTypeInstance instance(PeacodTypeInstance... args);

    public abstract TypeNode factory(TypeNode... args);
}
