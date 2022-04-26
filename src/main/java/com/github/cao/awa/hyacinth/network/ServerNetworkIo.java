package com.github.cao.awa.hyacinth.network;

import com.github.cao.awa.hyacinth.network.connection.ClientConnection;
import com.github.cao.awa.hyacinth.network.connection.RateLimitedConnection;
import com.github.cao.awa.hyacinth.network.handler.decode.DecoderHandler;
import com.github.cao.awa.hyacinth.network.handler.encode.PacketEncoder;
import com.github.cao.awa.hyacinth.network.handler.prepender.SizePrepender;
import com.github.cao.awa.hyacinth.network.handler.query.legacy.LegacyQueryHandler;
import com.github.cao.awa.hyacinth.network.handler.splitter.SplitterHandler;
import com.github.cao.awa.hyacinth.network.packet.s2c.disconnect.DisconnectS2CPacket;
import com.github.cao.awa.hyacinth.network.state.NetworkSide;
import com.github.cao.awa.hyacinth.network.text.LiteralText;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.network.handler.handshake.ServerHandshakeNetworkHandler;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ServerNetworkIo {
    public static final Lazy<NioEventLoopGroup> DEFAULT_CHANNEL = new Lazy<>(() -> new NioEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build())));
    public static final Lazy<EpollEventLoopGroup> EPOLL_CHANNEL = new Lazy<>(() -> new EpollEventLoopGroup(0, Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build())));
    private static final Logger LOGGER = LogManager.getLogger("NetworkIO");
    final MinecraftServer server;
    final List<ClientConnection> connections = Collections.synchronizedList(Lists.newArrayList());
    private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
    public volatile boolean active;

    public ServerNetworkIo(MinecraftServer server) {
        this.server = server;
        this.active = true;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void bind(@Nullable String address, int port) throws UnknownHostException {
        bind(InetAddress.getByName(address), port);
    }

        public void bind(@Nullable InetAddress address, int port) {
        synchronized (this.channels) {
            Lazy<?> lazy;
            Class class_;
//            if (Epoll.isAvailable() && this.server.isUsingNativeTransport()) {
//                class_ = EpollServerSocketChannel.class;
//                lazy = EPOLL_CHANNEL;
//                LOGGER.info("Using epoll channel type");
//            } else {
            class_ = NioServerSocketChannel.class;
            lazy = DEFAULT_CHANNEL;
            LOGGER.info("Using default channel type");
//            }
            this.channels.add(new ServerBootstrap().channel(class_).childHandler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel channel) {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                    } catch (ChannelException channelException) {

                    }
                    System.out.println("connecting?");
                    channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new LegacyQueryHandler(ServerNetworkIo.this)).addLast("splitter", new SplitterHandler()).addLast("decoder", new DecoderHandler(NetworkSide.SERVERBOUND)).addLast("prepender", new SizePrepender()).addLast("encoder", new PacketEncoder(NetworkSide.CLIENTBOUND));
                    int i = ServerNetworkIo.this.server.getRateLimit();
                    ClientConnection clientConnection = i > 0 ? new RateLimitedConnection(i) : new ClientConnection(NetworkSide.SERVERBOUND);
                    connections.add(clientConnection);
                    channel.pipeline().addLast("packet_handler", clientConnection);
                    clientConnection.setPacketListener(new ServerHandshakeNetworkHandler(server, clientConnection));
                    System.out.println("connected?");
                }
            }).group((EventLoopGroup) lazy.get()).localAddress(address, port).bind().syncUninterruptibly());
        }
    }
    public void stop() {
        this.active = false;
        for (ChannelFuture channelFuture : this.channels) {
            try {
                channelFuture.channel().close().sync();
            } catch (InterruptedException interruptedException) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }

    public void tick() {
        synchronized (this.connections) {
            Iterator<ClientConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                ClientConnection clientConnection = iterator.next();
                if (clientConnection.hasChannel())
                    continue;
                if (clientConnection.isOpen()) {
                    try {
                        clientConnection.tick();
                    } catch (Exception exception) {
                        LOGGER.warn("Failed to handle packet for {}", clientConnection.getAddress(), exception);
                        LiteralText text = new LiteralText("Internal server error");
                        clientConnection.send(new DisconnectS2CPacket(text), future -> clientConnection.disconnect(text));
                        clientConnection.disableAutoRead();
                    }
                    continue;
                }
                iterator.remove();
                clientConnection.handleDisconnection();
            }
        }
    }

//    public MinecraftServer getServer() {
//        return this.server;
//    }

//    public List<ClientConnection> getConnections() {
//        return this.connections;
//    }

    static class DelayingChannelInboundHandler extends ChannelInboundHandlerAdapter {
        private static final Timer TIMER = new HashedWheelTimer();
        private final int baseDelay;
        private final int extraDelay;
        private final List<Packet> packets = Lists.newArrayList();

        public DelayingChannelInboundHandler(int baseDelay, int extraDelay) {
            this.baseDelay = baseDelay;
            this.extraDelay = extraDelay;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            this.delay(ctx, msg);
        }

        private void delay(ChannelHandlerContext ctx, Object msg) {
            int i = this.baseDelay + (int) (Math.random() * (double) this.extraDelay);
            this.packets.add(new Packet(ctx, msg));
            TIMER.newTimeout(this::forward, i, TimeUnit.MILLISECONDS);
        }

        private void forward(Timeout timeout) {
            Packet packet = this.packets.remove(0);
            packet.context.fireChannelRead(packet.message);
        }

        static class Packet {
            public final ChannelHandlerContext context;
            public final Object message;

            public Packet(ChannelHandlerContext context, Object message) {
                this.context = context;
                this.message = message;
            }
        }
    }
}