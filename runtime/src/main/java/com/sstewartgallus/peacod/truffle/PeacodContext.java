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
package com.sstewartgallus.peacod.truffle;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.sstewartgallus.peacod.truffle.nodes.PeacodRootNode;
import com.sstewartgallus.peacod.truffle.nodes.action.intrinsics.AddNode;
import com.sstewartgallus.peacod.truffle.nodes.action.intrinsics.IfNode;
import com.sstewartgallus.peacod.truffle.nodes.value.ThunkValueNode;
import com.sstewartgallus.peacod.truffle.runtime.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PeacodContext {
    // fixme.. don't use
    public static final PeacodType LONG_VALUE = PeacodType.of(0);
    public static final PeacodType INT_VALUE = PeacodType.of(0);
    public static final PeacodType SHORT_VALUE = PeacodType.of(0);
    public static final PeacodType BYTE_VALUE = PeacodType.of(0);
    public static final PeacodType BOOLEAN_VALUE = PeacodType.of(0);

    private static final PeacodTypeInstance BOOL_INSTANCE = INT_VALUE.instance();
    private static final PeacodTypeInstance INT_INSTANCE = INT_VALUE.instance();


    private static final PeacodType BOX = PeacodType.of(1);
    private static final PeacodType FUNCTION_VALUE = PeacodType.of(2);

    // fixme.. handle generics properly?
    public static final Fn LESS_THAN_FUNCTION = new Fn(
            fn(Arrays.asList(INT_INSTANCE, INT_INSTANCE), BOOL_INSTANCE),
            Truffle.getRuntime().createCallTarget(new PeacodRootNode("lessThan") {

                @Override
                public String graph() {
                    return getName();
                }

                @Override
                public Object execute(VirtualFrame frame) {
                    throw null;
                }
            }));
    public static final Fn MUL_FUNCTION = new Fn(
            fn(Arrays.asList(INT_INSTANCE, INT_INSTANCE), INT_INSTANCE),
            Truffle.getRuntime().createCallTarget(new PeacodRootNode("mul") {
                @Override
                public String graph() {
                    return getName();
                }

                @Override
                public Object execute(VirtualFrame frame) {
                    throw null;
                }
            }));
    public static final Fn SUB_FUNCTION = new Fn(
            fn(Arrays.asList(INT_INSTANCE, INT_INSTANCE), INT_INSTANCE),
            Truffle.getRuntime().createCallTarget(new PeacodRootNode("sub") {

                @Override
                public String graph() {
                    return getName();
                }

                @Override
                public Object execute(VirtualFrame frame) {
                    throw null;
                }
            }));
    private Map<String, PeacodType> TYPE_DEF_MAP;
    private Map<String, Lib> LIBRARY_MAP = new HashMap<>();

    PeacodContext() {
    }

    static PeacodTypeInstance fn(List<PeacodTypeInstance> arguments, PeacodTypeInstance result) {
        for (var ii = arguments.size() - 1; ii >= 0; --ii) {
            result = FUNCTION_VALUE.instance(arguments.get(ii), result);
        }
        return result;
    }

    public Lib lookupLibrary(String library) {
        return LIBRARY_MAP.get(library);
    }

    public PeacodType lookup(String literal) {
        return TYPE_DEF_MAP.get(literal);
    }

    void initialize() {
        TYPE_DEF_MAP = new HashMap<>();
        TYPE_DEF_MAP.put("boolean", BOOLEAN_VALUE);
        TYPE_DEF_MAP.put("byte", BYTE_VALUE);
        TYPE_DEF_MAP.put("short", SHORT_VALUE);
        TYPE_DEF_MAP.put("int", INT_VALUE);
        TYPE_DEF_MAP.put("long", LONG_VALUE);
        TYPE_DEF_MAP.put("fn", FUNCTION_VALUE);
        TYPE_DEF_MAP.put("box", BOX);

        // fixme.. give types?
        Map<String, Definition> integersMap = new HashMap<>();
        integersMap.put("intAdd", IntrinsicDefinition.of("intAdd", 0, 2,
                (typeArgs, args, k) -> k.apply(ThunkValueNode.of(AddNode.of(args.get(0), args.get(1))))));

        Map<String, Definition> builtinsMap = new HashMap<>();
        builtinsMap.put("if", IntrinsicDefinition.of("if", 0, 3,
                (typeArgs, args, k) -> IfNode.of(args.get(0), k.apply(args.get(1)), k.apply(args.get(2)))));

        LIBRARY_MAP.put("peacod.lang.Integers", Lib.of(integersMap));
        LIBRARY_MAP.put("peacod.lang.Builtin", Lib.of(builtinsMap));
    }

    void putLibrary(String name, Lib lib) {
        LIBRARY_MAP.put(name, lib);
    }
}
