package com.sstewartgallus.peacod.truffle.plato;

public final class FunctionTag<A, B> implements TypeTag<V<A, V<B, Fun<A, B>>>> {
    private static final FunctionTag FUNCTION_TAG = new FunctionTag();

    public static <A, B> FunctionTag<A, B> function() {
        return FUNCTION_TAG;
    }

    @Override
    public String toString() {
        return "(→)";
    }
}