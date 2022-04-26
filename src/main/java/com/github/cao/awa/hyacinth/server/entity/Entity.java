package com.github.cao.awa.hyacinth.server.entity;

import com.github.cao.awa.hyacinth.math.*;
import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.box.Box;
import com.github.cao.awa.hyacinth.math.vec.*;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.command.output.CommandOutput;
import com.github.cao.awa.hyacinth.server.entity.data.*;
import com.github.cao.awa.hyacinth.server.entity.like.EntityLike;
import com.github.cao.awa.hyacinth.server.entity.listener.EntityChangeListener;
import com.github.cao.awa.hyacinth.server.entity.player.*;
import com.github.cao.awa.hyacinth.server.entity.pose.*;
import com.github.cao.awa.hyacinth.server.world.*;
import net.minecraft.Nameable;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.Stream;

public abstract class Entity implements Nameable, EntityLike, CommandOutput {
    private static final AtomicInteger CURRENT_ID = new AtomicInteger();
    protected final Random random = new Random();
    private final EntityType<?> type;
    private int id = CURRENT_ID.incrementAndGet();
    protected UUID uuid = Mathematics.randomUuid(this.random);
    private float yaw;
    private float pitch;
    public float prevYaw;
    public float prevPitch;
    protected static final TrackedData<EntityPose> POSE = DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.ENTITY_POSE);

    protected void setRotation(float yaw, float pitch) {
        this.setYaw(yaw % 360.0f);
        this.setPitch(pitch % 360.0f);
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    private World world;

    public Entity(EntityType<?> type, World world) {
        this.type = type;
                this.world = world;
        //        this.dimensions = type.getDimensions();
        //        this.pos = Vec3d.ZERO;
        //        this.blockPos = BlockPos.ORIGIN;
        //        this.chunkPos = ChunkPos.ORIGIN;
        //        this.trackedPosition = Vec3d.ZERO;
        //        this.dataTracker = new DataTracker(this);
        //        this.dataTracker.startTracking(FLAGS, (byte)0);
        //        this.dataTracker.startTracking(AIR, this.getMaxAir());
        //        this.dataTracker.startTracking(NAME_VISIBLE, false);
        //        this.dataTracker.startTracking(CUSTOM_NAME, Optional.empty());
        //        this.dataTracker.startTracking(SILENT, false);
        //        this.dataTracker.startTracking(NO_GRAVITY, false);
        //        this.dataTracker.startTracking(POSE, EntityPose.STANDING);
        //        this.dataTracker.startTracking(FROZEN_TICKS, 0);
        //        this.initDataTracker();
        //        this.setPosition(0.0, 0.0, 0.0);
        //        this.standingEyeHeight = this.getEyeHeight(EntityPose.STANDING, this.dimensions);
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public BlockPos getBlockPos() {
        return null;
    }

    @Override
    public Box getBoundingBox() {
        return null;
    }

    @Override
    public void setListener(EntityChangeListener var1) {

    }

    @Override
    public Stream<? extends EntityLike> streamSelfAndPassengers() {
        return null;
    }

    @Override
    public Stream<? extends EntityLike> streamPassengersAndSelf() {
        return null;
    }

    @Override
    public void setRemoved(RemovalReason var1) {

    }

    @Override
    public boolean shouldSave() {
        return false;
    }


    @Override
    public void sendSystemMessage(Text var1, UUID var2) {

    }

    @Override
    public boolean shouldReceiveFeedback() {
        return false;
    }

    @Override
    public boolean shouldTrackOutput() {
        return false;
    }

    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return false;
    }

    @Override
    public Text getName() {
        return null;
    }

    public EntityType<?> getType() {
        return this.type;
    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (POSE.equals(data)) {
            this.calculateDimensions();
        }
    }

    public void calculateDimensions() {
//        boolean bl;
//        EntityDimensions entityDimensions2;
//        EntityDimensions entityDimensions = this.dimensions;
//        EntityPose entityPose = this.getPose();
//        this.dimensions = entityDimensions2 = this.getDimensions(entityPose);
//        this.standingEyeHeight = this.getEyeHeight(entityPose, entityDimensions2);
//        this.refreshPosition();
//        boolean bl2 = bl = (double)entityDimensions2.width <= 4.0 && (double)entityDimensions2.height <= 4.0;
//        if (!(this.world.isClient || this.firstUpdate || this.noClip || !bl || !(entityDimensions2.width > entityDimensions.width) && !(entityDimensions2.height > entityDimensions.height) || this instanceof PlayerEntity)) {
//            Vec3d vec3d = this.getPos().add(0.0, (double)entityDimensions.height / 2.0, 0.0);
//            double d = (double)Math.max(0.0f, entityDimensions2.width - entityDimensions.width) + 1.0E-6;
//            double e = (double)Math.max(0.0f, entityDimensions2.height - entityDimensions.height) + 1.0E-6;
//            VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(vec3d, d, e, d));
//            this.world.findClosestCollision(this, voxelShape, vec3d, entityDimensions2.width, entityDimensions2.height, entityDimensions2.width).ifPresent(pos -> this.setPosition(pos.add(0.0, (double)(-entityDimensions.height) / 2.0, 0.0)));
//        }
    }

    public enum RemovalReason {
        KILLED(true, false), DISCARDED(true, false), UNLOADED_TO_CHUNK(false, true), UNLOADED_WITH_PLAYER(false, false), CHANGED_DIMENSION(false, false);

        private final boolean destroy;
        private final boolean save;

        RemovalReason(boolean destroy, boolean save) {
            this.destroy = destroy;
            this.save = save;
        }

        public boolean shouldDestroy() {
            return this.destroy;
        }

        public boolean shouldSave() {
            return this.save;
        }
    }
}
