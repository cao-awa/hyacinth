package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.google.common.collect.Lists;

//public class EntityEquipmentUpdateS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private static final byte field_33342 = -128;
//    private final int id;
//    private final List<Pair<EquipmentSlot, ItemStack>> equipmentList;
//
//    public EntityEquipmentUpdateS2CPacket(int id, List<Pair<EquipmentSlot, ItemStack>> equipmentList) {
//        this.id = id;
//        this.equipmentList = equipmentList;
//    }
//
//    public EntityEquipmentUpdateS2CPacket(PacketByteBuf buf) {
//        byte i;
//        this.id = buf.readVarInt();
//        EquipmentSlot[] equipmentSlots = EquipmentSlot.values();
//        this.equipmentList = Lists.newArrayList();
//        do {
//            i = buf.readByte();
//            EquipmentSlot equipmentSlot = equipmentSlots[i & 0x7F];
//            ItemStack itemStack = buf.readItemStack();
//            this.equipmentList.add(Pair.of(equipmentSlot, itemStack));
//        } while ((i & 0xFFFFFF80) != 0);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.id);
//        int i = this.equipmentList.size();
//        for (int j = 0; j < i; ++j) {
//            Pair<EquipmentSlot, ItemStack> pair = this.equipmentList.get(j);
//            EquipmentSlot equipmentSlot = pair.getFirst();
//            boolean bl = j != i - 1;
//            int k = equipmentSlot.ordinal();
//            buf.writeByte(bl ? k | 0xFFFFFF80 : k);
//            buf.writeItemStack(pair.getSecond());
//        }
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getId() {
//        return this.id;
//    }
//
//    public List<Pair<EquipmentSlot, ItemStack>> getEquipmentList() {
//        return this.equipmentList;
//    }
//}
//
