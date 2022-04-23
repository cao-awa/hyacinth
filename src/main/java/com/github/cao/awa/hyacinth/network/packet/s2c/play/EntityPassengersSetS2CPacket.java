package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class EntityPassengersSetS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int id;
//    private final int[] passengerIds;
//
//    public EntityPassengersSetS2CPacket(Entity entity) {
//        this.id = entity.getId();
//        List<Entity> list = entity.getPassengerList();
//        this.passengerIds = new int[list.size()];
//        for (int i = 0; i < list.size(); ++i) {
//            this.passengerIds[i] = list.get(i).getId();
//        }
//    }
//
//    public EntityPassengersSetS2CPacket(PacketByteBuf buf) {
//        this.id = buf.readVarInt();
//        this.passengerIds = buf.readIntArray();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        buf.writeIntArray(this.passengerIds);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int[] getPassengerIds() {
//        return this.passengerIds;
//    }
//
//    public int getId() {
//        return this.id;
//    }
//}

