package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.github.cao.awa.hyacinth.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//public class SpectatorTeleportC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final UUID targetUuid;
//
//    public SpectatorTeleportC2SPacket(UUID targetUuid) {
//        this.targetUuid = targetUuid;
//    }
//
//    public SpectatorTeleportC2SPacket(PacketByteBuf buf) {
//        this.targetUuid = buf.readUuid();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeUuid(this.targetUuid);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onSpectatorTeleport(this);
//    }
//
//    @Nullable
//    public Entity getTarget(ServerWorld world) {
//        return world.getEntity(this.targetUuid);
//    }
//}
//
