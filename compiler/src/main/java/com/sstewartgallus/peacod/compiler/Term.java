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

abstract class Term {
    // FIXME https://crypto.stanford.edu/~blynn/lambda/pts.html just unify terms ands types (on the backend only though)

    abstract Tag tag();

    enum Tag {
        LIT_BOOLEAN,
        LIT_INT,
        LIT_LONG,

        APPLY,
        REFERENCE,
        LOAD_ARG
    }

    static final class Reference {
        final String library;
        final String name;

        Reference(String library, String name) {
            this.library = library;
            this.name = name;
        }
    }

    static final class TermReference extends Term {
        final Reference reference;
        final TypeScheme scheme;

        // Maybe leave until later?
        final List<Type> typeArguments;

        TermReference(Reference reference, TypeScheme type,
                      List<Type> typeArguments) {
            this.reference = Objects.requireNonNull(reference);
            this.scheme = Objects.requireNonNull(type);
            this.typeArguments = Objects.requireNonNull(typeArguments);
        }

        @Override
        Tag tag() {
            return Tag.REFERENCE;
        }

        @Override
        public String toString() {
            return reference + " : " + scheme;
        }
    }

    // FIXME (add scheme?)
    static final class Apply extends Term {
        final Term function;
        final List<Term> arguments;


        Apply(Term function, List<Term> arguments) {
            this.function = function;
            this.arguments = arguments;
        }

        @Override
        Tag tag() {
            return Tag.APPLY;
        }

        @Override
        public String toString() {
            return "(" + function + " " + arguments + ")";
        }
    }

    static class Variable extends Term {
        final int argIndex;
        final Type type;

        Variable(int argIndex, Type type) {
            this.type = Objects.requireNonNull(type);
            this.argIndex = argIndex;
        }

        @Override
        Tag tag() {
            return Tag.LOAD_ARG;
        }

        @Override
        public String toString() {
            return "(load " + argIndex + ") : " + type;
        }
    }

    static abstract class Literal extends Term {
    }

    static class LitBoolean extends Literal {
        final boolean value;

        LitBoolean(boolean value) {
            this.value = value;
        }

        @Override
        Tag tag() {
            return Tag.LIT_BOOLEAN;
        }
    }

    static final class LitInt extends Literal {
        final int value;

        LitInt(int value) {
            this.value = value;
        }

        @Override
        Tag tag() {
            return Tag.LIT_INT;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    static final class LitLong extends Literal {
        final long value;

        LitLong(long value) {
            this.value = value;
        }

        @Override
        Tag tag() {
            return Tag.LIT_LONG;
        }
    }
}