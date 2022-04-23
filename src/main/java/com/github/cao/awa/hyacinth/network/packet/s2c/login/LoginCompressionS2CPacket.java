package com.github.cao.awa.hyacinth.network.packet.s2c.login;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.ClientLoginPacketListener;

public class LoginCompressionS2CPacket
        implements Packet<ClientLoginPacketListener> {
    private final int compressionThreshold;

    public LoginCompressionS2CPacket(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    public LoginCompressionS2CPacket(PacketByteBuf buf) {
        this.compressionThreshold = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.compressionThreshold);
    }

    @Override
    public void apply(ClientLoginPacketListener clientLoginPacketListener) {
        clientLoginPacketListener.onCompression(this);
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
}

