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
import java.util.*;
import java.util.function.Function;

public final class Compiler {

    static final Env.Var<Dict<Definition>> DEF = new Env.Var<>("def");
    static final Env.Var<Dict<Declaration>> DECL = new Env.Var<>("decl");

    private static final Dict<Function<Form, Effect<Expander.Value>>> DEFAULT_EXPR =
            new Dict<Function<Form, Effect<Expander.Value>>>()
                    .put("enum", BuiltinSpecial::data)
                    .put("struct", BuiltinSpecial::data)
                    .put("class", BuiltinSpecial::data)

                    .put("define", BuiltinSpecial::define)
                    .put("declare", BuiltinSpecial::declare)

                    .put("boolean", Expander.typeOp(new Expander.KindSig(0, (l) -> BuiltinTerms.BOOL_VALUE)))
                    .put("int", Expander.typeOp(new Expander.KindSig(0, (l) -> BuiltinTerms.INT_VALUE)))

                    .put("fn", BuiltinTerms::functionType)

                    .put("if", Expander.op(BuiltinTerms.IF))

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
        Form forms = Parser.parseForms(reader);

        Env env = Expander.expandForms(DEFAULT_ENV, forms);

        Dict<Definition> definitions = env.get(DEF);


        Map<String, TypedDefinition> defMap = new HashMap<>();
        for (Map.Entry<String, Definition> entry : definitions.entrySet()) {
            String name = entry.getKey();
            Definition def = entry.getValue();

            Set<Constraint> constraints = new HashSet<>();
            Type exprType = Constraints.termType(def.expr, constraints);

            Type type = def.type;
            List<Type> arguments = def.arguments;

            Type wrappedType;
            if (arguments.size() > 0) {
                List<Type> sig = new ArrayList<>(arguments);
                sig.add(exprType);
                wrappedType = BuiltinTerms.fn(sig.toArray(new Type[0]));
            } else {
                wrappedType = exprType;
            }

            constraints.add(Constraint.ofEqual(wrappedType, type));

            Map<Type, Type> solutions = Constraints.solve(constraints);

            TypedDefinition typed = TypedDefinition.of(
                    def.typeArguments,
                    Constraints.resolve(solutions, type),
                    Constraints.decorate(solutions, def.expr));

            defMap.put(name, typed);
        }

        return Serializer.serializeLibrary(defMap);
    }

}
