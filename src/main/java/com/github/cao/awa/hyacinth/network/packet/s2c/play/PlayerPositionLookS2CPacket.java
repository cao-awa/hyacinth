package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.util.EnumSet;
import java.util.Set;

public class PlayerPositionLookS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final Set<Flag> flags;
    private final int teleportId;
    private final boolean shouldDismount;

    public PlayerPositionLookS2CPacket(double x, double y, double z, float yaw, float pitch, Set<Flag> flags, int teleportId, boolean shouldDismount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
        this.teleportId = teleportId;
        this.shouldDismount = shouldDismount;
    }

    public PlayerPositionLookS2CPacket(PacketByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.flags = Flag.getFlags(buf.readUnsignedByte());
        this.teleportId = buf.readVarInt();
        this.shouldDismount = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(Flag.getBitfield(this.flags));
        buf.writeVarInt(this.teleportId);
        buf.writeBoolean(this.shouldDismount);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
        
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    public boolean shouldDismount() {
        return this.shouldDismount;
    }

    public Set<Flag> getFlags() {
        return this.flags;
    }

    public static enum Flag {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);

        private final int shift;

        private Flag(int shift) {
            this.shift = shift;
        }

        private int getMask() {
            return 1 << this.shift;
        }

        private boolean isSet(int mask) {
            return (mask & this.getMask()) == this.getMask();
        }

        public static Set<Flag> getFlags(int mask) {
            EnumSet<Flag> set = EnumSet.noneOf(Flag.class);
            for (Flag flag : Flag.values()) {
                if (!flag.isSet(mask)) continue;
                set.add(flag);
            }
            return set;
        }

        public static int getBitfield(Set<Flag> flags) {
            int i = 0;
            for (Flag flag : flags) {
                i |= flag.getMask();
            }
            return i;
        }
    }
}

