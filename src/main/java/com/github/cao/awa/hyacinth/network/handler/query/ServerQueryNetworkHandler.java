package com.github.cao.awa.hyacinth.network.handler.query;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryPongS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryPingC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.query.QueryRequestC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.query.ServerQueryPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.query.QueryResponseS2CPacket;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.MinecraftServer;

public class ServerQueryNetworkHandler implements ServerQueryPacketListener {
    private static final Text REQUEST_HANDLED = new LiteralText("multiplayer.status.request_handled");
    private final MinecraftServer server;
    private final ClientConnection connection;
    private boolean responseSent;

    public ServerQueryNetworkHandler(MinecraftServer server, ClientConnection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void onDisconnected(Text reason) {
    }

    @Override
    public ClientConnection getConnection() {
        return this.connection;
    }

    @Override
    public void onPing(QueryPingC2SPacket packet) {
        this.connection.send(new QueryPongS2CPacket(packet.getStartTime()));
        this.connection.disconnect(REQUEST_HANDLED);
    }

    @Override
    public void onRequest(QueryRequestC2SPacket packet) {
        if (this.responseSent) {
            this.connection.disconnect(REQUEST_HANDLED);
            return;
        }
        this.responseSent = true;
        this.connection.send(new QueryResponseS2CPacket(this.server.getServerMetadata()));
    }
}
