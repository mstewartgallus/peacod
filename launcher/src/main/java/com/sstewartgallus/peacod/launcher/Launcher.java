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
package com.sstewartgallus.peacod.launcher;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

final class Launcher {

    private static final String PEACOD = "peacod";

    /**
     * The main entry point.
     */
    public static void main(String[] args) throws IOException {
        var result = executeSource(args);
        System.exit(result);
    }

    private static int executeSource(String[] args) throws IOException {
        Source source;
        Map<String, String> options = new HashMap<>();
        String file = null;
        for (var arg : args) {
            if (parseOption(options, arg)) {
                continue;
            }

            if (file == null) {
                file = arg;
            }
        }
        Context context;
        var err = System.err;
        try {
            context = Context
                    .newBuilder(PEACOD)
                    .in(System.in)
                    .out(System.out)
                    .options(options)
                    .build();
        } catch (IllegalArgumentException e) {
            err.println(e.getMessage());
            return 1;
        }

        if (file == null) {
            source = Source.newBuilder(PEACOD, new InputStreamReader(System.in), "<stdin>").build();
        } else {
            source = Source.newBuilder(PEACOD, new File(file)).build();
        }

        try {
            var library = context.eval(source);
            var main = library.getMember("main");
            if (null == main) {
                err.println("No procedure main () defined in PEACOD source file.");
                return 1;
            }

            // Dead code elimination only applies if it is warmed up
            for (long ii = 0; ii < 1000; ++ii) {
                main.execute().execute();
            }

            var start = System.currentTimeMillis();
            var result = main.execute().execute().execute();
            var end = System.currentTimeMillis();

            System.out.println(result.toString() + " in " + (end - start) + "ms");
            return 0;
        } catch (PolyglotException ex) {
            if (ex.isInternalError()) {
                // for internal errors we print the full stack trace
                ex.printStackTrace();
            } else {
                err.println(ex.getMessage());
            }
            return 1;
        } finally {
            context.close();
        }
    }

    private static boolean parseOption(Map<String, String> options, String arg) {
        if (arg.length() <= 2 || !arg.startsWith("--")) {
            return false;
        }
        var eqIdx = arg.indexOf('=');
        String key;
        String value;
        if (eqIdx < 0) {
            key = arg.substring(2);
            value = null;
        } else {
            key = arg.substring(2, eqIdx);
            value = arg.substring(eqIdx + 1);
        }

        if (value == null) {
            value = "true";
        }
        options.put(key, value);
        return true;
    }

}
