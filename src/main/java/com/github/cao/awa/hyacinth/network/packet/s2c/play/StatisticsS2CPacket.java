//package com.github.cao.awa.hyacinth.network.packet.s2c.play;
//
//import com.github.cao.awa.hyacinth.network.packet.Packet;
//import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
//import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
//import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
//
//import java.rmi.registry.Registry;
//
//public class StatisticsS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Object2IntMap<Stat<?>> stats;
//
//    public StatisticsS2CPacket(Object2IntMap<Stat<?>> stats) {
//        this.stats = stats;
//    }
//
//    public StatisticsS2CPacket(PacketByteBuf buf2) {
//        this.stats = buf2.readMap(Object2IntOpenHashMap::new, buf -> {
//            int i = buf.readVarInt();
//            int j = buf.readVarInt();
//            return StatisticsS2CPacket.getStat((StatType) Registry.STAT_TYPE.get(i), j);
//        }, PacketByteBuf::readVarInt);
//    }
//
//    private static <T> Stat<T> getStat(StatType<T> statType, int id) {
//        return statType.getOrCreateStat(statType.getRegistry().get(id));
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//    }
//
//    @Override
//    public void write(PacketByteBuf buf2) {
//        buf2.writeMap(this.stats, (buf, stat) -> {
//            buf.writeVarInt(Registry.STAT_TYPE.getRawId(stat.getType()));
//            buf.writeVarInt(this.getStatNetworkId((Stat)stat));
//        }, PacketByteBuf::writeVarInt);
//    }
//
//    private <T> int getStatNetworkId(Stat<T> stat) {
//        return stat.getType().getRegistry().getRawId(stat.getValue());
//    }
//
//    public Map<Stat<?>, Integer> getStatMap() {
//        return this.stats;
//    }
//}
//
