package com.sstewartgallus.peacod.truffle.plato;

import java.util.function.Function;

/**
 * This is intended to be pristine source language untainted by compiler stuff.
 * <p>
 * Any processing should happen AFTER this step.
 */
public interface Type<X> {

    public static final Type<Integer> INTEGER_TYPE = NominalType.ofTag(new TypeTag<Integer>() {
    });
    public static final Type<Object[]> ARRAY_TYPE = NominalType.ofTag(new TypeTag<Object[]>() {
    });

    static <A, B> Type<V<A, B>> v(Function<Type<A>, Type<B>> f) {
        return new ForallType<>(f);
    }

    static <A, B> Type<B> apply(Type<V<A, B>> f, Type<A> x) {
        if (f instanceof ForallType) {
            return ((ForallType<A, B>) f).f().apply(x);
        }
        return new TypeApplyType<>(f, x);
    }

    default <B> Term<Fun<U<X>, B>> l(Function<Term<U<X>>, Term<B>> f) {
        return new SimpleLambdaTerm<>(this, f);
    }

    default <B> Type<Fun<X, B>> to(Type<B> range) {
        return new TypeApplyType<>(new TypeApplyType<>(Helper.function(), this), range);
    }

    default Type<U<X>> thunk() {
        return new TypeApplyType<>(Helper.thunk(), this);
    }
}

class Helper {

    static final NominalType THUNK = NominalType.ofTag(ThunkTag.thunk());
    static final NominalType FUNCTION = NominalType.ofTag(FunctionTag.function());

    static <A> Type<V<A, U<A>>> thunk() {
        return THUNK;
    }

    static <A, B> Type<V<A, V<B, Fun<A, B>>>> function() {
        return FUNCTION;
    }
}
