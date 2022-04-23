package com.github.cao.awa.hyacinth.network.handler.play;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.MinecraftServer;

public class ServerPlayNetworkHandler {
    private ClientConnection connection;
    private MinecraftServer server;

    public void disconnect(Text reason) {
        this.connection.send(new DisconnectS2CPacket(reason), future -> this.connection.disconnect(reason));
        this.connection.disableAutoRead();
        this.server.submitAndJoin(this.connection::handleDisconnection);
    }
}
