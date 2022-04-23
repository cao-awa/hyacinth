package com.github.cao.awa.hyacinth.identity;

import it.unimi.dsi.fastutil.Hash;

public enum IdentityHashStrategy implements Hash.Strategy<Object> {
    INSTANCE;


    @Override
    public int hashCode(Object object) {
        return System.identityHashCode(object);
    }

    @Override
    public boolean equals(Object object, Object object2) {
        return object == object2;
    }
}