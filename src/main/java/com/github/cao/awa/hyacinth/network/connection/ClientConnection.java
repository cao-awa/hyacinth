package com.github.cao.awa.hyacinth.network.connection;

import com.github.cao.awa.hyacinth.math.Mathematics;
import com.github.cao.awa.hyacinth.network.OffThreadException;
import com.github.cao.awa.hyacinth.network.encryption.PacketDecryptor;
import com.github.cao.awa.hyacinth.network.encryption.PacketEncryptor;
import com.github.cao.awa.hyacinth.network.handler.login.ServerLoginNetworkHandler;
import com.github.cao.awa.hyacinth.network.handler.play.ServerPlayNetworkHandler;
import com.github.cao.awa.hyacinth.network.packet.Packet;
import com.github.cao.awa.hyacinth.network.packet.PacketDeflater;
import com.github.cao.awa.hyacinth.network.packet.PacketInflater;
import com.github.cao.awa.hyacinth.network.packet.listener.PacketListener;
import com.github.cao.awa.hyacinth.network.state.NetworkSide;
import com.github.cao.awa.hyacinth.network.state.NetworkState;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.google.common.collect.Queues;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import java.net.SocketAddress;
import java.util.Queue;

public class ClientConnection extends SimpleChannelInboundHandler<Packet<?>> {
    public static final AttributeKey<NetworkState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
    private static final Logger LOGGER = LogManager.getLogger("Connection");
    private final Queue<QueuedPacket> packetQueue = Queues.newConcurrentLinkedQueue();
    private final NetworkSide side;
    private Channel channel;
    private SocketAddress address;
    private PacketListener packetListener;
    private int packetsSentCounter;
    private float averagePacketsSent;
    private float averagePacketsReceived;
    private int packetsReceivedCounter;
    private boolean encrypted = false;
    private boolean disconnected = false;
    private Text disconnectReason;
    private int ticks;

    public ClientConnection(NetworkSide side) {
        this.side = side;
    }

    public float getAveragePacketsReceived() {
        return this.averagePacketsReceived;
    }

    private static <T extends PacketListener> void handlePacket(Packet<T> packet, T listener) {
        packet.apply(listener);
    }

    public boolean hasChannel() {
        return this.channel == null;
    }

    public void tick() {
        this.sendQueuedPackets();
        if (this.packetListener instanceof ServerLoginNetworkHandler) {
            ((ServerLoginNetworkHandler) this.packetListener).tick();
        }
//        if(this.packetListener instanceof ServerPlayNetworkHandler) {
//            ((ServerPlayNetworkHandler) this.packetListener).tick();
//        }
        if (! this.isOpen() && ! this.disconnected) {
            this.handleDisconnection();
        }
        if (this.channel != null) {
            this.channel.flush();
        }
        if (this.ticks++ % 20 == 0) {
            this.updateStats();
        }
    }

    public void handleDisconnection() {
        if (this.channel == null || this.channel.isOpen()) {
            return;
        }
        if (this.disconnected) {
            LOGGER.warn("handleDisconnection() called twice");
        } else {
            this.disconnected = true;
            if (this.getDisconnectReason() != null) {
                this.getPacketListener().onDisconnected(this.getDisconnectReason());
            } else if (this.getPacketListener() != null) {
                this.getPacketListener().onDisconnected(new TranslatableText("multiplayer.disconnect.generic"));
            }
        }
    }

    public PacketListener getPacketListener() {
        return this.packetListener;
    }

    public void setPacketListener(PacketListener listener) {
        Validate.notNull(listener, "packetListener");
        packetListener = listener;
    }

    @Nullable
    public Text getDisconnectReason() {
        return this.disconnectReason;
    }

    private void sendQueuedPackets() {
        if (channel == null || ! channel.isOpen()) {
            return;
        }
        synchronized (packetQueue) {
            QueuedPacket queuedPacket;
            while ((queuedPacket = packetQueue.poll()) != null) {
                sendImmediately(queuedPacket.packet, queuedPacket.callback);
            }
        }
    }

    private void sendImmediately(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback) {
        NetworkState networkState = NetworkState.getPacketHandlerState(packet);
        NetworkState networkState2 = getState();
        ++ packetsSentCounter;
        if (networkState2 != networkState) {
            LOGGER.debug("Disabled auto read");
            channel.config().setAutoRead(false);
        }
        if (channel.eventLoop().inEventLoop()) {
            sendInternal(packet, callback, networkState, networkState2);
        } else {
            channel.eventLoop().execute(() -> sendInternal(packet, callback, networkState, networkState2));
        }
    }

    private void sendInternal(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback, NetworkState packetState, NetworkState currentState) {
        if (packetState != currentState) {
            this.setState(packetState);
        }
        ChannelFuture channelFuture = this.channel.writeAndFlush(packet);
        if (callback != null) {
            channelFuture.addListener(callback);
        }
        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private NetworkState getState() {
        return this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();
    }

    public void setState(NetworkState state) {
        channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(state);
        channel.config().setAutoRead(true);
    }

    protected void updateStats() {
        this.averagePacketsSent = Mathematics.lerp(0.75f, this.packetsSentCounter, this.averagePacketsSent);
        this.averagePacketsReceived = Mathematics.lerp(0.75f, this.packetsReceivedCounter, this.averagePacketsReceived);
        this.packetsSentCounter = 0;
        this.packetsReceivedCounter = 0;
    }

    public boolean isOpen() {
        return channel != null && channel.isOpen();
    }

    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    public void send(Packet<?> packet) {
        send(packet, null);
    }

    public void send(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback) {
        if (isOpen()) {
            sendQueuedPackets();
            sendImmediately(packet, callback);
        } else {
            packetQueue.add(new QueuedPacket(packet, callback));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
        this.channel = context.channel();
        this.address = this.channel.remoteAddress();
        try {
            this.setState(NetworkState.HANDSHAKING);
        } catch (Throwable throwable) {
            LOGGER.fatal(throwable);
        }
    }

    public void setupEncryption(Cipher decryptionCipher, Cipher encryptionCipher) {
        this.encrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecryptor(decryptionCipher));
        this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncryptor(encryptionCipher));
    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet packet) {
        if (channel.isOpen()) {
            try {
                System.out.println(packet.getClass().getName());
                ClientConnection.handlePacket(packet, packetListener);
            } catch (OffThreadException offThreadException) {
            } catch (ClassCastException classCastException) {
                LOGGER.error("Received {} that couldn't be processed", packet.getClass(), classCastException);
                disconnect(new TranslatableText("multiplayer.disconnect.invalid_packet"));
            }
            ++ packetsReceivedCounter;
        }
    }

    public void disconnect(Text disconnectReason) {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
            this.disconnectReason = disconnectReason;
        }
    }

    public SocketAddress getAddress() {
        return address;
    }

    /**
     * Sets the compression threshold of this connection.
     *
     * <p>Packets over the threshold in size will be written as a {@code 0}
     * byte followed by contents, while compressed ones will be written as
     * a var int for the decompressed size followed by the compressed contents.
     *
     * <p>The connections on the two sides must have the same compression
     * threshold, or compression errors may result.
     *
     * @param compressionThreshold
     *         the compression threshold, in number of bytes
     * @param rejectsBadPackets
     *         whether this connection may abort if a compressed packet with a bad size is received
     */
    public void setCompressionThreshold(int compressionThreshold, boolean rejectsBadPackets) {
        if(compressionThreshold > -1) {
            if(this.channel.pipeline().get("decompress") instanceof PacketInflater) {
                ((PacketInflater) this.channel.pipeline().get("decompress")).setCompressionThreshold(compressionThreshold, rejectsBadPackets);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new PacketInflater(compressionThreshold, rejectsBadPackets));
            }
            if(this.channel.pipeline().get("compress") instanceof PacketDeflater) {
                ((PacketDeflater) this.channel.pipeline().get("compress")).setCompressionThreshold(compressionThreshold);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new PacketDeflater(compressionThreshold));
            }
        } else {
            if(this.channel.pipeline().get("decompress") instanceof PacketInflater) {
                this.channel.pipeline().remove("decompress");
            }
            if(this.channel.pipeline().get("compress") instanceof PacketDeflater) {
                this.channel.pipeline().remove("compress");
            }
        }
    }

}
