package com.github.cao.awa.hyacinth.network.packet.c2s.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ServerPlayPacketListener;
import net.minecraft.util.identifier.Identifier;

public class CustomPayloadC2SPacket
implements Packet<ServerPlayPacketListener> {
    private static final int MAX_PAYLOAD_SIZE = Short.MAX_VALUE;
    public static final Identifier BRAND = new Identifier("brand");
    private final Identifier channel;
    private final PacketByteBuf data;

    public CustomPayloadC2SPacket(Identifier channel, PacketByteBuf data) {
        this.channel = channel;
        this.data = data;
    }

    public CustomPayloadC2SPacket(PacketByteBuf buf) {
        this.channel = buf.readIdentifier();
        int i = buf.readableBytes();
        if (i < 0 || i > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
        this.data = new PacketByteBuf(buf.readBytes(i));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.channel);
        buf.writeBytes(this.data);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        serverPlayPacketListener.onCustomPayload(this);
        this.data.release();
    }

    public Identifier getChannel() {
        return this.channel;
    }

    public PacketByteBuf getData() {
        return this.data;
    }
}

