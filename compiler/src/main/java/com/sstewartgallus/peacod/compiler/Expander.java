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

import java.util.*;
import java.util.function.Function;

class Expander {
    static final Env.Var<Dict<Function<Form, Effect<Value>>>> EXPR = new Env.Var<>("nodemaker");

    static Env expandForms(Env env, Form forms) {
        while (forms instanceof Form.Cons) {
            Form.Cons c = (Form.Cons) forms;

            Form form = c.head;
            forms = c.tail;

            String headString = formHead(form);

            Function<Form, Effect<Value>> command = env.get(EXPR).get(headString);
            if (null == command) {
                throw new Error("no top level binding for " + headString + " in environment " + env);
            }
            Effect.Results<Value> results = command.apply(form).execute(env);
            env = results.env;
            // ignore the type
        }
        return env;
    }

    static Effect.Results<Value> expand(Env env, Form expr) {
        for (; ; ) {
            if (expr instanceof Form.Atom) {
                String atom = ((Form.Atom) expr).value;

                // FIXME Find a good way to handle such a hook?
                notint:
                {
                    int x;
                    wasint:
                    {
                        try {
                            x = Integer.parseInt(atom);
                            break wasint;
                        } catch (NumberFormatException ignored) {
                        }
                        break notint;
                    }
                    return new Effect.Results<>(env, Expander.Value.of(new Term.LitInt(x)));
                }
            } else if (expr instanceof Form.Cons) {
                Form.Cons compound = ((Form.Cons) expr);
                Form head = compound.head;
                Form tail = compound.tail;

                if (tail instanceof Form.Empty) {
                    expr = head;
                    continue;
                }
            }

            String headName = formHead(expr);
            Function<Form, Effect<Value>> maker = env.get(EXPR).get(headName);
            if (null == maker) {
                throw new Error("form " + headName + " not found!");
            }
            return maker.apply(expr).execute(env);
        }
    }

    private static String formHead(Form form) {
        String headString;
        if (form instanceof Form.Cons) {
            Form.Cons c = (Form.Cons) form;
            Form head = c.head;
            headString = ((Form.Atom) head).value;
        } else if (form instanceof Form.Atom) {
            headString = ((Form.Atom) form).value;
        } else {
            throw new Error("unimpl");
        }
        return headString;
    }

    static Function<Form, Effect<Value>> typeOp(KindSig sig) {
        int arity = sig.arity;
        Function<List<Type>, Type> f = sig.transformation;
        return (g) -> (env) -> {
            Form form = g;

            List<Type> args = new ArrayList<>();
            while (form instanceof Form.Cons) {
                Form.Cons c = (Form.Cons) form;
                Form head = c.head;
                form = c.tail;

                Effect.Results<Value> results = expand(env, head);
                env = results.env;

                args.add(results.result.type());
            }

            if (args.size() != arity) {
                throw new Error("wrong " + args + "  arity " + arity);
            }
            Type value = f.apply(args);
            return new Effect.Results<>(env, Value.of(value));

        };
    }

    static Function<Form, Effect<Value>> op(Signature signature) {
        Type type = signature.type;
        String libraryName = signature.libraryName;
        String name = signature.name;
        List<Type> typeParameters = signature.typeParameters;

        return (g) -> (env) -> {
            Form form = g;

            Form.Cons c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            List<Term> args = new ArrayList<>();
            while (form instanceof Form.Cons) {
                c = (Form.Cons) form;
                Form head = c.head;
                form = c.tail;

                Effect.Results<Value> results = expand(env, head);
                env = results.env;

                args.add(results.result.expr());
            }

            Map<Type, Type> solutions = new HashMap<>();
            List<Type> typeArgsCopy = new ArrayList<>();
            for (Type typeArg : typeParameters) {
                Type fresh = new Type.Hole();
                typeArgsCopy.add(fresh);
                solutions.put(typeArg, fresh);
            }

            Type freshType = Constraints.resolve(solutions, type);

            Term value = new Term.Get(libraryName, name, freshType, typeArgsCopy);
            if (args.size() > 0) {
                value = new Term.Apply(value, args);
            }
            return new Effect.Results<>(env, Value.of(value));

        };
    }

    enum ValueType {
        EXPR,
        TYPE
    }

    interface Value {
        static Value of(Term expr) {
            return (ExprValue) () -> expr;
        }

        static Value of(Type expr) {
            return (TypeValue) () -> expr;
        }

        ValueType tag();

        default Type type() {
            throw new Error("unimpl");
        }

        default Term expr() {
            throw new Error("unimpl");
        }
    }

    @FunctionalInterface
    interface ExprValue extends Value {
        default ValueType tag() {
            return ValueType.EXPR;
        }

        Term expr();
    }

    @FunctionalInterface
    interface TypeValue extends Value {
        default ValueType tag() {
            return ValueType.TYPE;
        }

        Type type();
    }

    static final class KindSig {
        final int arity;
        final Function<List<Type>, Type> transformation;

        KindSig(int arity, Function<List<Type>, Type> transformation) {
            this.arity = arity;
            this.transformation = transformation;
        }
    }
}
