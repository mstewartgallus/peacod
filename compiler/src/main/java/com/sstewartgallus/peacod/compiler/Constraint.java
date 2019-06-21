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

abstract class Constraint {
    static Constraint ofEqual(Type left, Type right) {
        return new Equality(left, right);
    }

    abstract Tag tag();

    enum Tag {
        EQUALITY
    }

    static final class Equality extends Constraint {
        final Type left;
        final Type right;

        private Equality(Type left, Type right) {
            this.left = left;
            this.right = right;
        }

        @Override
        Tag tag() {
            return Tag.EQUALITY;
        }

        @Override
        public String toString() {
            return left + " = " + right;
        }
    }
}
