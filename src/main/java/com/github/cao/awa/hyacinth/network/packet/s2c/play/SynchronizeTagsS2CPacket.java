//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//
//public class SynchronizeTagsS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Map<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> groups;
//
//    public SynchronizeTagsS2CPacket(Map<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> groups) {
//        this.groups = groups;
//    }
//
//    public SynchronizeTagsS2CPacket(PacketByteBuf buf2) {
//        this.groups = buf2.readMap(buf -> RegistryKey.ofRegistry(buf.readIdentifier()), TagGroup.Serialized::fromBuf);
//    }
//
//    @Override
//    public void write(PacketByteBuf buf2) {
//        buf2.writeMap(this.groups, (buf, registryKey) -> buf.writeIdentifier(registryKey.getValue()), (buf, serializedGroup) -> serializedGroup.writeBuf((PacketByteBuf)buf));
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    public Map<RegistryKey<? extends Registry<?>>, TagGroup.Serialized> getGroups() {
//        return this.groups;
//    }
//}
//
