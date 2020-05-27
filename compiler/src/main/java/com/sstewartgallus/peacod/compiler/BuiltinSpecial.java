package com.sstewartgallus.peacod.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class BuiltinSpecial {
    static Effect<Expander.Value> data(Form g) {
        return (env) -> {
            // fixme
            throw new Error("foo");
        };
    }

    static Effect<Expander.Value> let(Form g) {
        return (env) -> {
            var form = g;
            var c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            var expr = ((Form.Cons) (c.head)).head;
            var tail = ((Form.Cons) (c.head)).tail;

            // fixme.. give local environment to to define
            var bodyEnv = env;
            while (tail instanceof Form.Cons) {
                // fixme.. ignore result?
                bodyEnv = Expander.expand(bodyEnv, expr).env;
                expr = ((Form.Cons) tail).head;
                tail = ((Form.Cons) tail).tail;
            }
            System.err.println(c.tail);
            return new Effect.Results<>(env, Expander.expand(bodyEnv, c.tail).result);
        };
    }

    static Effect<Expander.Value> declare(Form g) {
        return (env) -> {
            var form = g;
            var c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            var methodHead = c.head;
            var body = c.tail;

            Form args;
            String defName;
            if (methodHead instanceof Form.Atom) {
                defName = ((Form.Atom) methodHead).value;
                args = Form.empty();
            } else {
                defName = ((Form.Atom) ((Form.Cons) methodHead).head).value;
                args = ((Form.Cons) methodHead).tail;
            }

            List<String> argStrs = new ArrayList<>();

            while (args instanceof Form.Cons) {
                c = (Form.Cons) args;
                var head = c.head;
                args = c.tail;

                argStrs.add(((Form.Atom) head).value);
            }

            List<Type.Hole> typeArgs;
            Type type;
            {
                typeArgs = new ArrayList<>();
                var bodyEnv = env
                        .transform(Expander.EXPR, (e) -> {
                            for (var arg : argStrs) {
                                var hole = new Type.Hole();
                                typeArgs.add(hole);
                                e = e.put(arg, (f) -> (env1) -> new Effect.Results<>(env1, Expander.Value.of(hole)));
                            }
                            return e;
                        });


                type = Expander.expand(bodyEnv, body).result.type();
            }

            var scheme = TypeScheme.over(typeArgs, type);
            var decl = Declaration.of(scheme);

// FIXME: Error if there is already a decl
            env = env.transform(Compiler.DECL, (e) -> e.put(defName, decl));

            var signature = Signature.of("", defName, scheme);

            var localCallFactory = Expander.op(signature);
            env = env.transform(Expander.EXPR, (e) -> e.put(defName, localCallFactory));
            return new Effect.Results<>(env, null);
        };
    }

    static Effect<Expander.Value> define(Form g) {
        return (env) -> {
            var form = g;
            var c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            var methodHead = c.head;
            var body = c.tail;

            Form args;
            String defName;
            if (methodHead instanceof Form.Atom) {
                defName = ((Form.Atom) methodHead).value;
                args = Form.empty();
            } else {
                defName = ((Form.Atom) ((Form.Cons) methodHead).head).value;
                args = ((Form.Cons) methodHead).tail;
            }

            List<String> argStrs = new ArrayList<>();
            while (args instanceof Form.Cons) {
                c = (Form.Cons) args;
                var head = c.head;
                args = c.tail;

                argStrs.add(((Form.Atom) head).value);
            }

            var decl = env.get(Compiler.DECL).get(defName);
            if (null == decl) {
                throw new Error("undeclared " + defName);
            }

            var scheme = decl.scheme;

            List<Type> arguments = new ArrayList<>();

            var bodyEnv = env
                    .transform(Expander.EXPR, (e) -> {
                        var ii = 0;
                        for (var arg : argStrs) {
                            var argIndex = ii++;
                            Type argType = new Type.Hole();
                            arguments.add(argType);
                            e = e.put(arg, loadArg(argIndex, argType));
                        }
                        return e;
                    });

            var expr = Expander.expand(bodyEnv, body);
            bodyEnv = expr.env;

            // fixme...
            var letEnv = bodyEnv.get(Compiler.LET);
            //   env = expr.env;
            var exprResult = expr.result.expr();

            // fixme put definition arity in ?
            var def = Definition.of(letEnv, scheme, arguments, exprResult);

            // FIXME, check for duplicate defs
            env = env.transform(Compiler.DEF, (e) -> e.put(defName, def));

            // FIXME Consider returning the definition?
            return new Effect.Results<>(env, null);
        };
    }

    private static Function<Form, Effect<Expander.Value>> loadArg(int argIndex, Type type) {
        // FIXME: Check arguments
        return (form) -> (env) -> {
            Term expr = new Term.Variable(argIndex, type);
            return new Effect.Results<>(env, Expander.Value.of(expr));
        };
    }

}
