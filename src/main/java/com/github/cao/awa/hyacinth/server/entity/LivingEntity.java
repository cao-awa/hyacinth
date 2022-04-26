package com.github.cao.awa.hyacinth.server.entity;

import com.github.cao.awa.hyacinth.server.world.*;

public abstract class LivingEntity extends Entity {
    public LivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }
}
