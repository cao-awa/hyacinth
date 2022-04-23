package com.github.cao.awa.hyacinth.server.entity;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.math.box.Box;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.command.output.CommandOutput;
import com.github.cao.awa.hyacinth.server.entity.like.EntityLike;
import com.github.cao.awa.hyacinth.server.entity.listener.EntityChangeListener;
import net.minecraft.Nameable;

import java.util.UUID;
import java.util.stream.Stream;

public abstract class Entity implements Nameable, EntityLike, CommandOutput {
    private int id;
    private UUID uuid;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
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
