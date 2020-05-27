package com.sstewartgallus.peacod.truffle.plato;

import com.oracle.truffle.api.CompilerDirectives;
import com.sstewartgallus.peacod.truffle.runtime.Id;

import java.util.Objects;
import java.util.function.Function;

@CompilerDirectives.ValueType
public final class SimpleLambdaTerm<A, B> extends LambdaTerm<A, B> {
    private final Function<Term<U<A>>, Term<B>> f;

    public SimpleLambdaTerm(Type<A> domain,
                            Function<Term<U<A>>, Term<B>> f) {
        super(domain);
        this.f = Objects.requireNonNull(f);
    }

    @Override
    public String toString() {
        var id = new Id();
        return "(\\" + id + " -> " + f.apply(new VarTerm<>(domain.thunk(), id)) + ")";
    }

    @Override
    public Term<B> apply(Term<U<A>> x) {
        return f.apply(x);
    }
}
