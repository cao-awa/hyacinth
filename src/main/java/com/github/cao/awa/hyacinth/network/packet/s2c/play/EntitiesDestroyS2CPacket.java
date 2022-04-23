package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

//public class EntitiesDestroyS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final IntList entityIds;
//
//    public EntitiesDestroyS2CPacket(IntList entityIds) {
//        this.entityIds = new IntArrayList(entityIds);
//    }
//
//    public EntitiesDestroyS2CPacket(int ... entityIds) {
//        this.entityIds = new IntArrayList(entityIds);
//    }
//
//    public EntitiesDestroyS2CPacket(PacketByteBuf buf) {
//        this.entityIds = buf.readIntList();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeIntList(this.entityIds);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public IntList getEntityIds() {
//        return this.entityIds;
//    }
//}
//
