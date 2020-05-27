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

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

import static java.io.StreamTokenizer.*;

class Parser {

    static Form parseForms(Reader reader) {
        var tokenizer = new StreamTokenizer(reader);
        tokenizer.resetSyntax();

        tokenizer.eolIsSignificant(true);

        tokenizer.whitespaceChars(' ', ' ');

        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');

        var specialcases = new char[]{'<', '-', '+', '*', '[', ']', '.', '-', '|', '>', ':', ',', '=', '?', '∀', '\\', '!'};
        for (var c : specialcases) {
            tokenizer.wordChars(c, c);
        }

        tokenizer.commentChar(';');
        tokenizer.quoteChar('"');

        tokenizer.ordinaryChar('(');
        tokenizer.ordinaryChar(')');

        List<List<Form>> stack = new ArrayList<>();
        List<Form> currentList = new ArrayList<>();
        loop:
        for (; ; ) {
            int token;
            try {
                token = tokenizer.nextToken();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Form val;
            switch (token) {
                default:
                    throw new Error("unknown token " + token);

                case TT_EOL:
                    // Just ignore for now but later use for line annotations
                    continue;

                case TT_EOF:
                    if (stack.isEmpty()) {
                        break loop;
                    }
                    throw new Error("early eof");

                case ')': {
                    if (stack.isEmpty()) {
                        break loop;
                    }

                    var popped = currentList;
                    currentList = stack.remove(stack.size() - 1);

                    val = Form.list(popped);
                    break;
                }

                case TT_WORD:
                    val = Form.atom(tokenizer.sval);
                    break;

                case '(':
                    stack.add(currentList);
                    currentList = new ArrayList<>();
                    continue;

                case '"':
                    // TODO: This is an ugly hack but how to get rid of?
                    val = Form.atom("\"" + tokenizer.sval + "\"");
                    break;
            }
            currentList.add(val);
        }

        return Form.list(currentList);
    }
}
