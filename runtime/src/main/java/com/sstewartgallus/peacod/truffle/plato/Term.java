package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.interop.TruffleObject;
import com.sstewartgallus.peacod.truffle.cbpv.Action;

import java.util.function.Function;

/**
 * The high level syntax for the core System F terms in my little language.
 * <p>
 * This is intended to be pristine source language untainted by compiler stuff.
 * <p>
 * Any processing should happen AFTER this step.
 * <p>
 * See https://gitlab.haskell.org/ghc/ghc/-/wikis/commentary/compiler/core-syn-type
 * and https://github.com/DanBurton/Blog/blob/master/Literate%20Haskell/SystemF.lhs
 * for inspiration.
 */
@CompilerDirectives.ValueType
public abstract class Term<A> implements TruffleObject {
    public static <A, B> Term<B> apply(Term<V<A, B>> f, Type<A> x) {
        return new TypeApplyTerm<>(f, x);
    }

    public static <A, B> Term<B> apply(Term<Fun<U<A>, B>> f, Term<A> x) {
        return new ApplyTerm<>(f, x);
    }

    public static <A, B> Term<V<A, B>> v(Function<Type<A>, Term<B>> f) {
        return new SimpleTypeLambdaTerm<>(f);
    }

    public Type<A> type() {
        throw null;
    }

    public Action<A> compile() {
        throw null;
    }
}