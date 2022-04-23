package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import com.github.cao.awa.hyacinth.server.world.World;

//public class UpdateCommandBlockMinecartC2SPacket
//implements Packet<ServerPlayPacketListener> {
//    private final int entityId;
//    private final String command;
//    private final boolean trackOutput;
//
//    public UpdateCommandBlockMinecartC2SPacket(int entityId, String command, boolean trackOutput) {
//        this.entityId = entityId;
//        this.command = command;
//        this.trackOutput = trackOutput;
//    }
//
//    public UpdateCommandBlockMinecartC2SPacket(PacketByteBuf buf) {
//        this.entityId = buf.readVarInt();
//        this.command = buf.readString();
//        this.trackOutput = buf.readBoolean();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.entityId);
//        buf.writeString(this.command);
//        buf.writeBoolean(this.trackOutput);
//    }
//
//    @Override
//    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
//        serverPlayPacketListener.onUpdateCommandBlockMinecart(this);
//    }
//
//    @Nullable
//    public CommandBlockExecutor getMinecartCommandExecutor(World world) {
//        Entity entity = world.getEntityById(this.entityId);
//        if (entity instanceof CommandBlockMinecartEntity) {
//            return ((CommandBlockMinecartEntity)entity).getCommandExecutor();
//        }
//        return null;
//    }
//
//    public String getCommand() {
//        return this.command;
//    }
//
//    public boolean shouldTrackOutput() {
//        return this.trackOutput;
//    }
//}
//
