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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class BuiltinTerms {
    static final Signature IF;
    static final Signature FORCE_IF;
    static final Signature FORCE;

    static final Type INT_VALUE;
    static final Type BOOL_VALUE;
    static final Signature LESS_THAN;
    static final Signature ADD;
    static final Signature SUB;
    static final Signature MUL;
    static final Type.Tag BOX;
    private static final String BUILTINS = "peacod.lang.Builtin";
    private static final String INTS_LIBRARY = "peacod.lang.Integers";
    private static final Type.Tag FUNCTION;

    static {
        BOOL_VALUE = Type.of(Type.Tag.of(BUILTINS, "boolean", 0));
        INT_VALUE = Type.of(Type.Tag.of(BUILTINS, "int", 0));
        FUNCTION = Type.Tag.of(BUILTINS, "fn", 2);
        BOX = Type.Tag.of(BUILTINS, "box", 1);
    }

    static {
        var a = new Type.Hole();
        FORCE_IF = Signature.of(BUILTINS, "!if", TypeScheme.over(
                Collections.singletonList(a),
                fn(Arrays.asList(BOOL_VALUE, box(a), box(a)), a)));
    }

    static {
        var a = new Type.Hole();
        IF = Signature.of(BUILTINS, "if", TypeScheme.over(
                Collections.singletonList(a),
                fn(Arrays.asList(BOOL_VALUE, a, a), a)));
    }

    static {
        var a = new Type.Hole();
        FORCE = Signature.of(BUILTINS, "!", TypeScheme.over(
                Collections.singletonList(a),
                fn(Collections.singletonList(box(a)), a)));
    }

    // FIXME put in integers class...
    static {
        LESS_THAN = Signature.of(INTS_LIBRARY, "intLessThan", fn(Arrays.asList(INT_VALUE, INT_VALUE), BOOL_VALUE));
        ADD = Signature.of(INTS_LIBRARY, "intAdd", fn(Arrays.asList(INT_VALUE, INT_VALUE), INT_VALUE));
        SUB = Signature.of(INTS_LIBRARY, "intSub", fn(Arrays.asList(INT_VALUE, INT_VALUE), INT_VALUE));
        MUL = Signature.of(INTS_LIBRARY, "intMul", fn(Arrays.asList(INT_VALUE, INT_VALUE), INT_VALUE));
    }

    static Effect<Expander.Value> trueVal(Form form) {
        return (env) -> new Effect.Results<>(env, Expander.Value.of(new Term.LitBoolean(true)));
    }

    static Effect<Expander.Value> falseVal(Form form) {
        return (env) -> new Effect.Results<>(env, Expander.Value.of(new Term.LitBoolean(false)));
    }

    static Effect<Expander.Value> functionType(Form g) {
        return (env) -> {
            var form = g;
            var c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            form = c.head;
            var tail = c.tail;

            List<Type> types = new ArrayList<>();
            if (form instanceof Form.Atom) {
                var expr = Expander.expand(env, form);
                env = expr.env;
                var type = expr.result.type();
                types.add(type);
            }
            while (form instanceof Form.Cons) {
                c = (Form.Cons) form;
                var head = c.head;
                form = c.tail;

                var expr = Expander.expand(env, head);
                env = expr.env;
                var type = expr.result.type();
                types.add(type);
            }

            var expr = Expander.expand(env, tail);
            env = expr.env;
            var type = expr.result.type();
            var t = fn(types, type);
            return new Effect.Results<>(env, Expander.Value.of(t));
        };
    }

    static Type box(Type type) {
        return Type.of(BOX, type);
    }

    static Type fn(List<Type> arguments, Type result) {
        for (var ii = arguments.size() - 1; ii >= 0; --ii) {
            result = Type.of(FUNCTION, arguments.get(ii), result);
        }
        return result;
    }

}
