package com.github.cao.awa.hyacinth.network.packet.s2c.play;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.play.ClientPlayPacketListener;
import net.minecraft.util.identifier.Identifier;
import org.jetbrains.annotations.Nullable;

public class SelectAdvancementTabS2CPacket
implements Packet<ClientPlayPacketListener> {
    @Nullable
    private final Identifier tabId;

    public SelectAdvancementTabS2CPacket(@Nullable Identifier tabId) {
        this.tabId = tabId;
    }

    @Override
    public void apply(ClientPlayPacketListener clientPlayPacketListener) {
    }

    public SelectAdvancementTabS2CPacket(PacketByteBuf buf) {
        this.tabId = buf.readBoolean() ? buf.readIdentifier() : null;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.tabId != null);
        if (this.tabId != null) {
            buf.writeIdentifier(this.tabId);
        }
    }

    @Nullable
    public Identifier getTabId() {
        return this.tabId;
    }
}

