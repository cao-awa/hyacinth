package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

//public class EndCombatS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int attackerId;
//    private final int timeSinceLastAttack;
//
//    public EndCombatS2CPacket(DamageTracker damageTracker) {
//        this(damageTracker.getBiggestAttackerId(), damageTracker.getTimeSinceLastAttack());
//    }
//
//    public EndCombatS2CPacket(int attackerId, int timeSinceLastAttack) {
//        this.attackerId = attackerId;
//        this.timeSinceLastAttack = timeSinceLastAttack;
//    }
//
//    public EndCombatS2CPacket(PacketByteBuf buf) {
//        this.timeSinceLastAttack = buf.readVarInt();
//        this.attackerId = buf.readInt();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.timeSinceLastAttack);
//        buf.writeInt(this.attackerId);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//}

