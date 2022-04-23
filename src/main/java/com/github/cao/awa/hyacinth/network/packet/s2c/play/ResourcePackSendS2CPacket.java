package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;
import org.jetbrains.annotations.Nullable;

public class ResourcePackSendS2CPacket
implements Packet<ClientPlayPacketListener> {
    public static final int MAX_HASH_LENGTH = 40;
    private final String url;
    private final String hash;
    private final boolean required;
    @Nullable
    private final Text prompt;

    public ResourcePackSendS2CPacket(String url, String hash, boolean required, @Nullable Text prompt) {
        if (hash.length() > 40) {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
        }
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    public ResourcePackSendS2CPacket(PacketByteBuf buf) {
        this.url = buf.readString();
        this.hash = buf.readString(40);
        this.required = buf.readBoolean();
        this.prompt = buf.readBoolean() ? buf.readText() : null;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.url);
        buf.writeString(this.hash);
        buf.writeBoolean(this.required);
        if (this.prompt != null) {
            buf.writeBoolean(true);
            buf.writeText(this.prompt);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public String getURL() {
        return this.url;
    }

    public String getSHA1() {
        return this.hash;
    }

    public boolean isRequired() {
        return this.required;
    }

    @Nullable
    public Text getPrompt() {
        return this.prompt;
    }
}

