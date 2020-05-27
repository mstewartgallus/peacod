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

import java.util.Arrays;
import java.util.Objects;

// FIXME USE Type Constructors...
abstract class Type {

    static Type of(Tag tag, Type... types) {
        return new Concrete(tag, types);
    }

    static final class Tag {
        final String library;
        final String name;
        // FiXME handle better
        final int arity;

        private Tag(String library, String name, int arity) {
            this.library = Objects.requireNonNull(library);
            this.name = Objects.requireNonNull(name);
            this.arity = arity;
        }

        static Tag of(String library, String name, int arity) {
            return new Tag(library, name, arity);
        }

        @Override
        public int hashCode() {
            return 31 * library.hashCode() + name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Tag)) {
                return false;
            }

            var tag = (Tag) obj;
            return
                    Objects.equals(library, tag.library)
                            && Objects.equals(name, tag.name);
        }

        public String toString() {
            return library + "." + name + "/" + arity;
        }
    }

    static final class Concrete extends Type {
        final Type[] types;
        final Tag tag;

        Concrete(Tag tag, Type... types) {
            if (tag.arity != types.length) {
                throw new Error("misapplication of " + tag + " to " + Arrays.toString(types));
            }
            this.tag = Objects.requireNonNull(tag);
            this.types = Objects.requireNonNull(types);
        }

        @Override
        public String toString() {
            if (types.length == 0) {
                return tag.name;
            }

            var buf = new StringBuilder();
            buf.append('(');
            buf.append(tag.name);

            for (var t : types) {
                buf.append(' ');
                buf.append(t);
            }
            buf.append(')');

            return buf.toString();
        }
    }

    static final class Hole extends Type {
        @Override
        public String toString() {
            return "Hole@" + hashCode();
        }
    }
}
