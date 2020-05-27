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
import java.util.List;
import java.util.function.Function;

class Expander {
    static final Env.Var<Dict<Function<Form, Effect<Value>>>> EXPR = new Env.Var<>("nodemaker");

    static Env expandForms(Env env, Form forms) {
        while (forms instanceof Form.Cons) {
            var c = (Form.Cons) forms;

            var form = c.head;
            forms = c.tail;

            var headString = formHead(form);

            var command = env.get(EXPR).get(headString);
            if (null == command) {
                throw new Error("no top level binding for " + headString + " in environment " + env);
            }
            var results = command.apply(form).execute(env);
            env = results.env;
            // ignore the scheme
        }
        return env;
    }

    static Effect.Results<Value> expand(Env env, Form expr) {
        for (; ; ) {
            if (expr instanceof Form.Atom) {
                var atom = ((Form.Atom) expr).value;

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
                var compound = ((Form.Cons) expr);
                var head = compound.head;
                var tail = compound.tail;

                if (tail instanceof Form.Empty) {
                    expr = head;
                    continue;
                }
            }

            var headName = formHead(expr);
            var maker = env.get(EXPR).get(headName);
            if (null == maker) {
                throw new Error("form " + headName + " not found!");
            }
            return maker.apply(expr).execute(env);
        }
    }

    private static String formHead(Form form) {
        String headString;
        if (form instanceof Form.Cons) {
            var c = (Form.Cons) form;
            var head = c.head;
            headString = ((Form.Atom) head).value;
        } else if (form instanceof Form.Atom) {
            headString = ((Form.Atom) form).value;
        } else {
            throw new Error("unimpl");
        }
        return headString;
    }

    static Function<Form, Effect<Value>> typeOp(Type.Tag tag) {
        var arity = tag.arity;
        return (g) -> (env) -> {
            var form = g;

            form = ((Form.Cons) form).tail;

            List<Type> args = new ArrayList<>();
            while (form instanceof Form.Cons) {
                var c = (Form.Cons) form;
                var head = c.head;
                form = c.tail;

                var results = expand(env, head);
                env = results.env;

                args.add(results.result.type());
            }

            if (args.size() != arity) {
                throw new Error("cannot apply " + args + " of size " + args.size() + " to " + tag + " of arity " + arity);
            }
            var value = Type.of(tag, args.toArray(new Type[0]));
            return new Effect.Results<>(env, Value.of(value));

        };
    }

    static Function<Form, Effect<Value>> op(Signature signature) {
        var libraryName = signature.libraryName;
        var name = signature.name;

        var scheme = signature.typeScheme;

        return (g) -> (env) -> {
            var form = g;

            // sometimes an atom..

            List<Term> args = new ArrayList<>();


            if (form instanceof Form.Cons) {
                var c = (Form.Cons) form;
                form = c.tail;
                c = (Form.Cons) form;

                while (form instanceof Form.Cons) {
                    c = (Form.Cons) form;
                    var head = c.head;
                    form = c.tail;

                    var results = expand(env, head);
                    env = results.env;

                    args.add(results.result.expr());
                }
            }

            List<Type> typeArgsCopy = new ArrayList<>();

            // fixme.. consider other use?
            var typeParameters = scheme.holes;

            for (Type ignored : typeParameters) {
                Type fresh = new Type.Hole();
                typeArgsCopy.add(fresh);
            }

            // fixme use one term..
            Term value = new Term.TermReference(new Term.Reference(libraryName, name), scheme, typeArgsCopy);
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
}
