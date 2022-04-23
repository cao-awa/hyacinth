package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;

import java.rmi.registry.Registry;

//public class OpenScreenS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final int syncId;
//    private final int screenHandlerId;
//    private final Text name;
//
//    public OpenScreenS2CPacket(int syncId, ScreenHandlerType<?> type, Text name) {
//        this.syncId = syncId;
//        this.screenHandlerId = Registry.SCREEN_HANDLER.getRawId(type);
//        this.name = name;
//    }
//
//    public OpenScreenS2CPacket(PacketByteBuf buf) {
//        this.syncId = buf.readVarInt();
//        this.screenHandlerId = buf.readVarInt();
//        this.name = buf.readText();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeVarInt(this.syncId);
//        buf.writeVarInt(this.screenHandlerId);
//        buf.writeText(this.name);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public int getSyncId() {
//        return this.syncId;
//    }
//
//    @Nullable
//    public ScreenHandlerType<?> getScreenHandlerType() {
//        return (ScreenHandlerType)Registry.SCREEN_HANDLER.get(this.screenHandlerId);
//    }
//
//    public Text getName() {
//        return this.name;
//    }
//}

