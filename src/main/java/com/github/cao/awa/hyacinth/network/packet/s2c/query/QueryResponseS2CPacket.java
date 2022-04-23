package com.github.cao.awa.hyacinth.network.packet.s2c.query;

import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.buf.PacketByteBuf;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ClientQueryPacketListener;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.style.Style;
import com.github.cao.awa.hyacinth.server.meta.ServerMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.enums.LowercaseEnumTypeAdapterFactory;
import net.minecraft.util.json.JsonHelper;

import java.lang.reflect.Type;

public class QueryResponseS2CPacket
        implements Packet<ClientQueryPacketListener> {
    private final ServerMetadata metadata;

    public QueryResponseS2CPacket(ServerMetadata metadata) {
        this.metadata = metadata;
    }

    public QueryResponseS2CPacket(PacketByteBuf buf) {
        //server do not deserialize here
        metadata = null;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.metadata.toJSONObject().toString());
    }

    @Override
    public void apply(ClientQueryPacketListener clientQueryPacketListener) {
        clientQueryPacketListener.onResponse(this);
    }

    public ServerMetadata getServerMetadata() {
        return this.metadata;
    }
}

