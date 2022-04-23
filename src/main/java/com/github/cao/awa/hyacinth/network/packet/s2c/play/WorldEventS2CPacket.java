package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.math.block.BlockPos;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class WorldEventS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final int eventId;
    private final BlockPos pos;
    private final int data;
    private final boolean global;

    public WorldEventS2CPacket(int eventId, BlockPos pos, int data, boolean global) {
        this.eventId = eventId;
        this.pos = pos.toImmutable();
        this.data = data;
        this.global = global;
    }

    public WorldEventS2CPacket(PacketByteBuf buf) {
        this.eventId = buf.readInt();
        this.pos = buf.readBlockPos();
        this.data = buf.readInt();
        this.global = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.eventId);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.data);
        buf.writeBoolean(this.global);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public boolean isGlobal() {
        return this.global;
    }

    public int getEventId() {
        return this.eventId;
    }

    public int getData() {
        return this.data;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

