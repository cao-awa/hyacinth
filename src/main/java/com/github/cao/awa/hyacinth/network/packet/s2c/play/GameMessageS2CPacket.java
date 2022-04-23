package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;

import java.awt.*;
import java.util.UUID;

/**
 * A packet used to send a game message to the client.
 * 
 * @see net.minecraft.server.network.ServerPlayerEntity#sendMessage(Text, MessageType, UUID)
 * @see net.minecraft.client.network.ClientPlayNetworkHandler#onGameMessage
 */
//public class GameMessageS2CPacket
//implements Packet<ClientPlayPacketListener> {
//    private final Text message;
//    private final MessageType type;
//    private final UUID sender;
//
//    public GameMessageS2CPacket(Text message, MessageType type, UUID sender) {
//        this.message = message;
//        this.type = type;
//        this.sender = sender;
//    }
//
//    public GameMessageS2CPacket(PacketByteBuf buf) {
//        this.message = buf.readText();
//        this.type = MessageType.byId(buf.readByte());
//        this.sender = buf.readUuid();
//    }
//
//    @Override
//    public void write(PacketByteBuf buf) {
//        buf.writeText(this.message);
//        buf.writeByte(this.type.getId());
//        buf.writeUuid(this.sender);
//    }
//
//    @Override
//    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
//
//    }
//
//    public Text getMessage() {
//        return this.message;
//    }
//
//    public MessageType getType() {
//        return this.type;
//    }
//
//    /**
//     * {@return {@linkplain net.minecraft.entity.Entity#getUuid the UUID of the
//     * entity} that sends the message or {@link net.minecraft.util.Util#NIL_UUID}
//     * if the message is not sent by an entity}
//     */
//    public UUID getSender() {
//        return this.sender;
//    }
//
//    @Override
//    public boolean isWritingErrorSkippable() {
//        return true;
//    }
//}
//
