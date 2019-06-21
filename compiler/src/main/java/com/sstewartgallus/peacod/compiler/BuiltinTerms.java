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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BuiltinTerms {

    static final Signature IF;
    static final Type INT_VALUE;
    static final Type BOOL_VALUE;
    static final Signature LESS_THAN;
    static final Signature ADD;
    static final Signature SUB;
    static final Signature MUL;

    private static final String BUILTINS = "peacod.lang.Builtin";
    private static final String INTS_LIBRARY = "peacod.lang.Integers";

    private static final Type.Tag[] FUNCTIONS;

    static {
        FUNCTIONS = new Type.Tag[255];
        for (int ii = 0; ii < FUNCTIONS.length; ++ii) {
            // FIXME, the names should be different?
            FUNCTIONS[ii] = Type.Tag.of(BUILTINS, "fn", ii + 1);
        }
    }

    static {
        BOOL_VALUE = Type.of(Type.Tag.of(BUILTINS, "boolean", 0));
        INT_VALUE = Type.of(Type.Tag.of(BUILTINS, "int", 0));
    }

    static {
        Type a = new Type.Hole();
        IF = Signature.of(BUILTINS, "if",
                fn(BOOL_VALUE, a, a, a),
                Collections.singletonList(a));
    }

    static Effect<Expander.Value> trueVal(Form form) {
        return (env) -> new Effect.Results<>(env, Expander.Value.of(new Term.LitBoolean(true)));
    }

    // FIXME put in integers class...
    static {
        LESS_THAN = Signature.of(INTS_LIBRARY, "intLessThan", fn(INT_VALUE, INT_VALUE,
                BOOL_VALUE));
        ADD = Signature.of(INTS_LIBRARY, "intAdd", fn(INT_VALUE, INT_VALUE,
                INT_VALUE));
        SUB = Signature.of(INTS_LIBRARY, "intSub", fn(INT_VALUE, INT_VALUE,
                INT_VALUE));
        MUL = Signature.of(INTS_LIBRARY, "intMul", fn(INT_VALUE, INT_VALUE,
                INT_VALUE));
    }

    static Effect<Expander.Value> falseVal(Form form) {
        return (env) -> new Effect.Results<>(env, Expander.Value.of(new Term.LitBoolean(false)));
    }

    static Effect<Expander.Value> functionType(Form g) {
        return (env) -> {
            Form form = g;
            Form.Cons c = (Form.Cons) form;
            form = c.tail;

            List<Type> types = new ArrayList<>();
            while (form instanceof Form.Cons) {
                c = (Form.Cons) form;
                Form head = c.head;
                form = c.tail;

                Effect.Results<Expander.Value> expr = Expander.expand(env, head);
                env = expr.env;
                Type type = expr.result.type();
                types.add(type);
            }

            Type t = fn(types.toArray(new Type[0]));
            return new Effect.Results<>(env, Expander.Value.of(t));
        };
    }

    static Type fn(Type... types) {
        return Type.of(FUNCTIONS[types.length - 1], types);
    }


}
