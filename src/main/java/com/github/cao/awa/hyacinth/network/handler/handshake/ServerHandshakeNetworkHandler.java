package com.github.cao.awa.hyacinth.network.handler.handshake;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.handler.login.ServerLoginNetworkHandler;
import com.github.cao.awa.hyacinth.network.handler.query.ServerQueryNetworkHandler;
import com.github.cao.awa.hyacinth.network.packet.c2s.handshake.HandshakeC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.ServerHandshakePacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.LoginDisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.state.NetworkState;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import org.apache.logging.log4j.*;

public class ServerHandshakeNetworkHandler implements ServerHandshakePacketListener {
    private static final Text IGNORING_STATUS_REQUEST_MESSAGE = new LiteralText("Ignoring status request");
    private final MinecraftServer server;
    private final ClientConnection connection;

    public ServerHandshakeNetworkHandler(MinecraftServer server, ClientConnection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void onHandshake(HandshakeC2SPacket packet) {
        switch (packet.getIntendedState()) {
            case LOGIN -> {
                this.connection.setState(NetworkState.LOGIN);
                if (packet.getProtocolVersion() == SharedConstants.getGameVersion().getProtocolVersion()) {
                    this.connection.setPacketListener(new ServerLoginNetworkHandler(this.server, this.connection));
                    break;
                }
                TranslatableText text = packet.getProtocolVersion() < 754 ? new TranslatableText("multiplayer.disconnect.outdated_client", SharedConstants.getGameVersion().getName()) : new TranslatableText("multiplayer.disconnect.incompatible", SharedConstants.getGameVersion().getName());
                this.connection.send(new LoginDisconnectS2CPacket(text));
                this.connection.disconnect(text);
            }
            case STATUS -> {
                if (this.server.acceptsStatusQuery()) {
                    this.connection.setState(NetworkState.STATUS);
                    this.connection.setPacketListener(new ServerQueryNetworkHandler(this.server, this.connection));
                    break;
                }
                this.connection.disconnect(IGNORING_STATUS_REQUEST_MESSAGE);
            }
            default -> {
                throw new UnsupportedOperationException("Invalid intention " + packet.getIntendedState());
            }
        }
    }

    @Override
    public void onDisconnected(Text reason) {
    }

    @Override
    public ClientConnection getConnection() {
        return this.connection;
    }
}
