package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class EntityTrackerUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    @Nullable
//    private final List<DataTracker.Entry<?>> trackedValues;
//
//    public EntityTrackerUpdateS2CPacket(int id, DataTracker tracker, boolean forceUpdateAll) {
//        this.id = id;
//        if (forceUpdateAll) {
//            this.trackedValues = tracker.getAllEntries();
//            tracker.clearDirty();
//        } else {
//            this.trackedValues = tracker.getDirtyEntries();
//        }
//    }
//
//    public EntityTrackerUpdateS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.trackedValues = DataTracker.deserializePacket(buf);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        DataTracker.entriesToPacket(this.trackedValues, buf);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    @Nullable
//    public List<DataTracker.Entry<?>> getTrackedValues() {
//        return this.trackedValues;
//    }
//
//    public int id() {
//        return this.id;
//    }
//}

