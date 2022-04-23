package com.github.cao.awa.hyacinth.network.handler.login;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import com.github.cao.awa.hyacinth.network.encryption.NetworkEncryptionException;
import com.github.cao.awa.hyacinth.network.encryption.NetworkEncryptionUtils;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginHelloC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.c2s.login.LoginKeyC2SPacket;
import com.github.cao.awa.hyacinth.network.packet.listener.ServerLoginPacketListener;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.LoginCompressionS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.LoginDisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.LoginHelloS2CPacket;
import com.github.cao.awa.hyacinth.network.packet.s2c.login.LoginSuccessS2CPacket;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.entity.player.PlayerEntity;
import com.github.cao.awa.hyacinth.server.entity.player.ServerPlayerEntity;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import net.minecraft.util.loggin.UncaughtExceptionLogger;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerLoginNetworkHandler implements ServerLoginPacketListener {
    static final Logger LOGGER = LogManager.getLogger("Handler:Login");
    private static final AtomicInteger NEXT_AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
    private static final Random RANDOM = new Random();
    public final ClientConnection connection;
    final MinecraftServer server;
    private final byte[] nonce = new byte[4];
    private LoginState state = LoginState.HELLO;
    @Nullable GameProfile profile;
    private int loginTicks;
    private ServerPlayerEntity delayedPlayer;

    public ServerLoginNetworkHandler(MinecraftServer server, ClientConnection connection) {
        this.server = server;
        this.connection = connection;
        RANDOM.nextBytes(this.nonce);
    }

    /**
     * Ticks this login network handler.
     *
     * <p>This accepts the player to the server if ready. If the state is delay
     * accept, it checks if the old player with the same UUID is gone and
     * admits the player.
     *
     * @apiNote This should only be called on the server thread.
     */
    public void tick() {
        if (this.state == LoginState.READY_TO_ACCEPT) {
            this.acceptPlayer();
        } else if(this.state == LoginState.DELAY_ACCEPT && this.server.getPlayerManager().getPlayer(this.profile.getId()) == null) {
            this.state = LoginState.READY_TO_ACCEPT;
            this.addToServer(this.delayedPlayer);
            this.delayedPlayer = null;
        }
//        if (this.loginTicks++ > 600) {
//            this.disconnect(new LiteralText("multiplayer.disconnect.slow_login"));
//        }
    }

    /**
     * Creates the player to be added to the server and adds it to the server.
     *
     * <p>If a player with the same UUID is in the world, it will create the
     * player and transition to the delay accept state.
     *
     * @apiNote This method should only be called on the server thread.
     */
    public void acceptPlayer() {
        Text text;
        if (! this.profile.isComplete()) {
            this.profile = this.toOfflineProfile(this.profile);
        }
        if ((text = this.server.getPlayerManager().checkCanJoin(this.connection.getAddress(), this.profile)) != null) {
            this.disconnect(text);
        } else {
            this.state = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionThreshold() > - 1) {
                this.connection.send(new LoginCompressionS2CPacket(this.server.getNetworkCompressionThreshold()), channelFuture -> this.connection.setCompressionThreshold(this.server.getNetworkCompressionThreshold(), true));
            }
            this.connection.send(new LoginSuccessS2CPacket(this.profile));
            ServerPlayerEntity serverPlayerEntity = this.server.getPlayerManager().getPlayer(this.profile.getId());
            try {
                ServerPlayerEntity serverPlayerEntity2 = this.server.getPlayerManager().createPlayer(this.profile);
                if (serverPlayerEntity != null) {
                    this.state = LoginState.DELAY_ACCEPT;
                    this.delayedPlayer = serverPlayerEntity2;
                } else {
                    this.addToServer(serverPlayerEntity2);
                }
            } catch (Exception serverPlayerEntity2) {
                LOGGER.error("Couldn't place player in world", serverPlayerEntity2);
                TranslatableText text2 = new TranslatableText("multiplayer.disconnect.invalid_player_data");
                this.connection.send(new DisconnectS2CPacket(text2));
                this.connection.disconnect(text2);
            }
        }
    }

    private void addToServer(ServerPlayerEntity player) {
        this.server.getPlayerManager().onPlayerConnect(this.connection, player);
    }

    @Override
    public void onHello(LoginHelloC2SPacket packet) {
        Validate.validState(this.state == LoginState.HELLO, "Unexpected hello packet");
        this.profile = packet.getProfile();
        if (this.server.isOnlineMode()) {
            this.state = LoginState.KEY;
            this.connection.send(new LoginHelloS2CPacket("", this.server.getKeyPair().getPublic().getEncoded(), this.nonce));
        } else {
            this.state = LoginState.READY_TO_ACCEPT;
        }
    }

    @Override
    public void onKey(LoginKeyC2SPacket packet) {
        String string;
        Object secretKey;
        Validate.validState(this.state == LoginState.KEY, "Unexpected key packet");
        PrivateKey privateKey = this.server.getKeyPair().getPrivate();
        try {
            if (! Arrays.equals(this.nonce, packet.decryptNonce(privateKey))) {
                throw new IllegalStateException("Protocol error");
            }
            secretKey = packet.decryptSecretKey(privateKey);
            Cipher cipher = NetworkEncryptionUtils.cipherFromKey(2, (Key) secretKey);
            Cipher cipher2 = NetworkEncryptionUtils.cipherFromKey(1, (Key) secretKey);
            string = new BigInteger(NetworkEncryptionUtils.generateServerId("", this.server.getKeyPair().getPublic(), (SecretKey) secretKey)).toString(16);
            this.state = LoginState.AUTHENTICATING;
            this.connection.setupEncryption(cipher, cipher2);
        } catch (NetworkEncryptionException secretKey2) {
            throw new IllegalStateException("Protocol error", secretKey2);
        }
        secretKey = new Thread("User Authenticator #" + NEXT_AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
            @Override
            public void run() {
                GameProfile gameProfile = ServerLoginNetworkHandler.this.profile;
                try {
                    ServerLoginNetworkHandler.this.profile = ServerLoginNetworkHandler.this.server.getSessionService().hasJoinedServer(new GameProfile(null, gameProfile.getName()), string, this.getClientAddress());
                    if (ServerLoginNetworkHandler.this.profile != null) {
                        LOGGER.info("UUID of player {} is {}", ServerLoginNetworkHandler.this.profile.getName(), ServerLoginNetworkHandler.this.profile.getId());
                        ServerLoginNetworkHandler.this.state = LoginState.READY_TO_ACCEPT;
                    } else {
                        ServerLoginNetworkHandler.this.disconnect(new TranslatableText("multiplayer.disconnect.unverified_username"));
                        LOGGER.error("Username '{}' tried to join with an invalid session", gameProfile.getName());
                    }
                } catch (AuthenticationUnavailableException authenticationUnavailableException) {
                    LOGGER.warn("Authentication servers are down but will let them in anyway!");
                    ServerLoginNetworkHandler.this.disconnect(new TranslatableText("multiplayer.disconnect.authservers_down"));
                    LOGGER.error("Couldn't verify username because servers are unavailable");
                }
            }

            @Nullable
            private InetAddress getClientAddress() {
                SocketAddress socketAddress = ServerLoginNetworkHandler.this.connection.getAddress();
                return ServerLoginNetworkHandler.this.server.shouldPreventProxyConnections() && socketAddress instanceof InetSocketAddress ? ((InetSocketAddress) socketAddress).getAddress() : null;
            }
        };
        ((Thread) secretKey).setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
        ((Thread) secretKey).start();
    }

    @Override
    public void onQueryResponse(LoginQueryResponseC2SPacket packet) {
        this.disconnect(new TranslatableText("multiplayer.disconnect.unexpected_query_response"));
    }

    public void disconnect(Text reason) {
        try {
            LOGGER.info("Disconnecting {}: {}", this.getConnectionInfo(), reason.getString());
            this.connection.send(new LoginDisconnectS2CPacket(reason));
            this.connection.disconnect(reason);
        } catch (Exception exception) {
            LOGGER.error("Error whilst disconnecting player", exception);
        }
    }

    public String getConnectionInfo() {
        if (this.profile != null) {
            return this.profile + " (" + this.connection.getAddress() + ")";
        }
        return String.valueOf(this.connection.getAddress());
    }

    @Override
    public void onDisconnected(Text reason) {
        LOGGER.info("{} lost connection: {}", this.getConnectionInfo(), reason.getString());
    }

    @Override
    public ClientConnection getConnection() {
        return this.connection;
    }

    protected GameProfile toOfflineProfile(GameProfile profile) {
        UUID uUID = PlayerEntity.getOfflinePlayerUuid(profile.getName());
        return new GameProfile(uUID, profile.getName());
    }
}
