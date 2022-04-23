package com.github.cao.awa.hyacinth.server.hit;

import com.github.cao.awa.hyacinth.math.vec.Vec3d;

public abstract class HitResult {
    protected final Vec3d pos;

    protected HitResult(Vec3d pos) {
        this.pos = pos;
    }

//    public double squaredDistanceTo(Entity entity) {
//        double d = this.pos.x - entity.getX();
//        double e = this.pos.y - entity.getY();
//        double f = this.pos.z - entity.getZ();
//        return d * d + e * e + f * f;
//    }

    public abstract Type getType();

    public Vec3d getPos() {
        return this.pos;
    }

    public static enum Type {
        MISS,
        BLOCK,
        ENTITY;

    }
}

