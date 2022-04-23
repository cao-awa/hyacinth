package com.github.cao.awa.hyacinth.network;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

@Deprecated
public class Lazy<T> {
    private final Supplier<T> delegate;
    private final Supplier<T> supplier;

    public Lazy(Supplier<T> delegate) {
        this.delegate = delegate;
        supplier = Suppliers.memoize(delegate::get);
    }

    public T get() {
        if (supplier != null) {
            return this.supplier.get();
        }
        return null;
    }
}

