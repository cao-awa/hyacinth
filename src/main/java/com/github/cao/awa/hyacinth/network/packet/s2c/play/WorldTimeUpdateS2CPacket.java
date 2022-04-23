package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

public class WorldTimeUpdateS2CPacket
implements Packet<ClientPlayPacketListener> {
    private final long time;
    private final long timeOfDay;

    public WorldTimeUpdateS2CPacket(long time, long timeOfDay, boolean doDaylightCycle) {
        this.time = time;
        long l = timeOfDay;
        if (!doDaylightCycle && (l = -l) == 0L) {
            l = -1L;
        }
        this.timeOfDay = l;
    }

    public WorldTimeUpdateS2CPacket(PacketByteBuf buf) {
        this.time = buf.readLong();
        this.timeOfDay = buf.readLong();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeLong(this.time);
        buf.writeLong(this.timeOfDay);
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public long getTime() {
        return this.time;
    }

    public long getTimeOfDay() {
        return this.timeOfDay;
    }
}

