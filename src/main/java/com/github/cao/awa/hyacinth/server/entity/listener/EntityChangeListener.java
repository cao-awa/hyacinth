package com.github.cao.awa.hyacinth.server.entity.listener;

import com.github.cao.awa.hyacinth.server.entity.Entity;

/**
 * A listener for an entity's changes associated to saving.
 *
 * <p>Each callback is associated with an {@link Entity}.
 */
public interface EntityChangeListener {
    /**
     * An empty listener.
     */
    EntityChangeListener NONE = new EntityChangeListener(){

        @Override
        public void updateEntityPosition() {
        }

        @Override
        public void remove(Entity.RemovalReason reason) {
        }
    };

    void updateEntityPosition();

    void remove(Entity.RemovalReason var1);
}

