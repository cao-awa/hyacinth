package com.github.cao.awa.hyacinth.server.entity.like;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.box.Box;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.github.cao.awa.hyacinth.server.entity.listener.EntityChangeListener;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * A prototype of entity that's suitable for entity manager to handle.
 */
public interface EntityLike {
    /**
     * {@return the network ID of this entity}
     *
     * <p>Compared to the {@linkplain #getUuid() UUID}, the integer network ID is
     * significantly smaller and more suitable for network transportation. However, it
     * is not persistent across game runs. For persistent purposes such as commands
     * or game data, use the UUID.
     */
    int getId();

    UUID getUuid();

    BlockPos getBlockPos();

    Box getBoundingBox();

    void setListener(EntityChangeListener var1);

    /**
     * Returns a stream consisting of this entity and its passengers recursively.
     * Each entity will appear before any of its passengers.
     *
     * <p>This may be less costly than {@link #streamPassengersAndSelf()} if the
     * stream's iteration would terminates fast, such as finding an arbitrary
     * match of entity in the passengers tree.
     *
     * @implNote The default implementation is not very efficient.
     *
     * @see #streamPassengersAndSelf()
     */
    Stream<? extends EntityLike> streamSelfAndPassengers();

    /**
     * Returns a stream consisting of this entity and its passengers in which
     * this entity's passengers are iterated before this entity.
     *
     * <p>Moreover, this stream guarantees that any entity only appears after
     * all its passengers have appeared in the stream. This is useful for
     * certain actions that must be applied on passengers before applying on
     * this entity.
     *
     * @implNote The default implementation is very costly.
     *
     * @see #streamSelfAndPassengers()
     */
    Stream<? extends EntityLike> streamPassengersAndSelf();

    void setRemoved(Entity.RemovalReason var1);

    boolean shouldSave();

    boolean isPlayer();
}

