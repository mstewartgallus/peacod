package com.sstewartgallus.peacod.truffle.runtime;

import com.oracle.truffle.api.library.GenerateLibrary;
import com.oracle.truffle.api.library.Library;
import com.sstewartgallus.peacod.truffle.plato.Term;

@GenerateLibrary
public abstract class LambdaLibrary extends Library {

    public boolean isLambda(Object receiver) {
        return false;
    }

    public abstract Term<?> apply(Object receiver, Term<?> value);
}