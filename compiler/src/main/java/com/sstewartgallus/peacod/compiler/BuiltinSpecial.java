package com.sstewartgallus.peacod.compiler;

import java.util.*;
import java.util.function.Function;

class BuiltinSpecial {
    static Effect<Expander.Value> data(Form g) {
        return (env) -> {
            // fixme
            throw new Error("foo");
        };
    }

    static Effect<Expander.Value> declare(Form g) {
        return (env) -> {
            Form form = g;
            Form.Cons c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            Form methodHead = c.head;
            Form body = c.tail;

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
                Form head = c.head;
                args = c.tail;

                argStrs.add(((Form.Atom) head).value);
            }

            List<Type> typeArgs;
            Type type;
            {
                typeArgs = new ArrayList<>();
                Env bodyEnv = env
                        .transform(Expander.EXPR, (e) -> {
                            for (String arg : argStrs) {
                                Type hole = new Type.Hole();
                                typeArgs.add(hole);
                                e = e.put(arg, (f) -> (env1) -> new Effect.Results<>(env1, Expander.Value.of(hole)));
                            }
                            return e;
                        });


                type = Expander.expand(bodyEnv, body).result.type();
            }

            Declaration decl = Declaration.of(typeArgs, type);

// FIXME: Error if there is already a decl
            env = env.transform(Compiler.DECL, (e) -> e.put(defName, decl));

            Signature signature = Signature.of(null, defName, type, typeArgs);

            Function<Form, Effect<Expander.Value>> localCallFactory = Expander.op(signature);
            env = env.transform(Expander.EXPR, (e) -> e.put(defName, localCallFactory));
            return new Effect.Results<>(env, null);
        };
    }

    static Effect<Expander.Value> define(Form g) {
        return (env) -> {
            Form form = g;
            Form.Cons c = (Form.Cons) form;
            form = c.tail;
            c = (Form.Cons) form;

            Form methodHead = c.head;
            Form body = c.tail;

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
                Form head = c.head;
                args = c.tail;

                argStrs.add(((Form.Atom) head).value);
            }

            Declaration decl = env.get(Compiler.DECL).get(defName);
            if (null == decl) {
                throw new Error("undeclared " + defName);
            }

            Type type = decl.result;

            List<Type> arguments = new ArrayList<>();

            Env bodyEnv = env
                    .transform(Expander.EXPR, (e) -> {
                        int ii = 0;
                        for (String arg : argStrs) {
                            int argIndex = ii++;
                            Type argType = new Type.Hole();
                            arguments.add(argType);
                            e = e.put(arg, loadArg(argIndex, argType));
                        }
                        return e;
                    });

            Effect.Results<Expander.Value> expr = Expander.expand(bodyEnv, body);
            env = expr.env;
            Term exprResult = expr.result.expr();

            // fixme put definition arity in...
            Definition def = Definition.of(decl.typeArguments, arguments, type, exprResult);

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
