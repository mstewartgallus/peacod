package com.sstewartgallus.peacod.truffle.plato;

public final class ThunkTag<A> implements TypeTag<V<A, U<A>>> {
    private static final ThunkTag FUNCTION_TAG = new ThunkTag();

    public static <A> ThunkTag<A> thunk() {
        return FUNCTION_TAG;
    }

    @Override
    public String toString() {
        return "U";
    }
}