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

import com.sstewartgallus.peacod.ast.LibraryDef;

import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Compiler {
    static final Env.Var<LetBindings> LET = new Env.Var<>("let");

    static final Env.Var<Dict<Definition>> DEF = new Env.Var<>("def");
    static final Env.Var<Dict<Declaration>> DECL = new Env.Var<>("decl");

    private static final Dict<Function<Form, Effect<Expander.Value>>> DEFAULT_EXPR =
            new Dict<Function<Form, Effect<Expander.Value>>>()
                    .put("enum", BuiltinSpecial::data)
                    .put("struct", BuiltinSpecial::data)
                    .put("class", BuiltinSpecial::data)

                    .put("let", BuiltinSpecial::let)

                    .put("define", BuiltinSpecial::define)
                    .put("declare", BuiltinSpecial::declare)

                    .put("boolean", (g) -> env -> new Effect.Results<>(env, Expander.Value.of(BuiltinTerms.BOOL_VALUE)))
                    .put("int", (g) -> env -> new Effect.Results<>(env, Expander.Value.of(BuiltinTerms.INT_VALUE)))

                    .put("box", Expander.typeOp(BuiltinTerms.BOX))

                    .put("fn", BuiltinTerms::functionType)

                    .put("!", Expander.op(BuiltinTerms.FORCE))
                    .put("if", Expander.op(BuiltinTerms.IF))
                    .put("!if", Expander.op(BuiltinTerms.FORCE_IF))

                    .put("+", Expander.op(BuiltinTerms.ADD))
                    .put("-", Expander.op(BuiltinTerms.SUB))
                    .put("*", Expander.op(BuiltinTerms.MUL))
                    .put("<", Expander.op(BuiltinTerms.LESS_THAN))

                    .put("true", BuiltinTerms::trueVal)
                    .put("false", BuiltinTerms::falseVal);

    private static final Env DEFAULT_ENV = Env.empty()
            .put(Expander.EXPR, DEFAULT_EXPR)
            .put(DECL, new Dict<>())
            .put(DEF, new Dict<>());

    private Compiler() {
    }

    public static LibraryDef compile(Reader reader) {
        var forms = Parser.parseForms(reader);

        var env = Expander.expandForms(DEFAULT_ENV, forms);

        var definitions = env.get(DEF);

        Map<String, TypedDefinition> defMap = new HashMap<>();
        for (var entry : definitions.entrySet()) {
            var name = entry.getKey();
            var def = entry.getValue();

            Set<Constraint> constraints = new HashSet<>();
            var exprType = Constraints.termType(def.expr, constraints);

            var arguments = def.arguments;

            Type wrappedType;
            if (arguments.size() > 0) {
                wrappedType = BuiltinTerms.fn(arguments, exprType);
            } else {
                wrappedType = exprType;
            }

            // fixme.. push down!
            var scheme = def.scheme;

            var type = scheme.type;
            constraints.add(Constraint.ofEqual(wrappedType, type));

            var solutions = Constraints.solve(constraints);

            var typed = TypedDefinition.of(
                    TypeScheme.over(scheme.holes, Constraints.resolve(solutions, scheme.type)),
                    def.arguments.size(),
                    Constraints.decorate(solutions, def.expr));

            defMap.put(name, typed);
        }

        return Serializer.serializeLibrary(defMap);
    }

}
