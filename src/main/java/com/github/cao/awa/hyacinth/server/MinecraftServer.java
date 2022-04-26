package com.github.cao.awa.hyacinth.server;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.github.cao.awa.hyacinth.network.ServerNetworkIo;
import com.github.cao.awa.hyacinth.server.command.output.CommandOutput;
import com.github.cao.awa.hyacinth.server.dimension.*;
import com.github.cao.awa.hyacinth.server.meta.ServerMetadata;
import com.github.cao.awa.hyacinth.server.player.manager.PlayerManager;
import com.github.cao.awa.hyacinth.server.task.ServerTask;
import com.github.cao.awa.hyacinth.server.world.ServerWorld;
import com.github.cao.awa.hyacinth.server.world.World;
import com.github.cao.awa.hyacinth.server.world.level.storage.LevelStorage;
import com.github.zhuaidadaya.rikaishinikui.handler.times.TimeUtil;
import com.google.common.collect.Maps;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.util.registry.*;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static com.github.cao.awa.hyacinth.server.dimension.DimensionType.OVERWORLD_ID;

public abstract class MinecraftServer extends ReentrantThreadExecutor<ServerTask> implements CommandOutput, AutoCloseable {
    public static final String VANILLA = "vanilla";
    public static final String HYACINTH = "hyacinth";
    private static final Logger LOGGER = LogManager.getLogger("Server");
    public final LevelStorage.Session session = null;
    // TODO: 2022/4/22 add to creator method and cancel null
    public final MinecraftSessionService sessionService = null;
    public final boolean onlineMode = false;
    @Nullable
    public final KeyPair keyPair = null;
    public final ServerMetadata metadata = new ServerMetadata();
    public final ServerNetworkIo networkIo;
    public final int compressionThreshold = 256;
    public final int rateLimit = 0;
    public final int maxPlayers = 20;
    public final Thread serverThread;
    public final long lastTimeReference = 0;
    public final boolean waitingForNextTick = false;
    public final long nextTickTimestamp = 0;
    protected final DynamicRegistryManager.Impl registryManager;
    private final Map<RegistryKey<World>, ServerWorld> worlds = Maps.newLinkedHashMap();
    private final Executor workerExecutor;
    public int ticks;
    public PlayerManager playerManager;
    private long timeReference;
    private short serverPort = 25565;
    private String serverHost = "127.0.0.1";

    public MinecraftServer(Thread serverThread, DynamicRegistryManager.Impl registryManager) throws UnknownHostException {
        super("Server");
        this.registryManager = registryManager;
        this.networkIo = new ServerNetworkIo(this);
        this.serverThread = serverThread;
        this.workerExecutor = Executors.newCachedThreadPool();

        this.getNetworkIo().bind(getServerHost(), this.getServerPort());
        setup();
    }

    public short getServerPort() {
        return serverPort;
    }

    public void setServerPort(short serverPort) {
        this.serverPort = serverPort;
    }

    @Nullable
    public ServerNetworkIo getNetworkIo() {
        return this.networkIo;
    }

    public void setup() {
        this.setupMetadata(this.metadata);
        playerManager = new PlayerManager(this, maxPlayers, registryManager);

        //        ServerWorld serverWorld = new ServerWorld(this, this.workerExecutor, this.session, serverWorldProperties, World.OVERWORLD, dimensionType, worldGenerationProgressListener, chunkGenerator, bl, m, list, true);
        ServerWorld serverWorld = new ServerWorld(this, this.workerExecutor, this.session, World.OVERWORLD, DimensionType.OVERWORLD, 0, false);
        this.worlds.put(World.OVERWORLD, serverWorld);
    }

    public void setupMetadata(ServerMetadata metadata) {
        try {
            Optional<File> optional = Optional.of(this.getFile("server-icon.png")).filter(File::isFile);
            if (! optional.isPresent()) {
                optional = this.session.getIconFile().map(Path::toFile).filter(File::isFile);
            }
            optional.ifPresent(file -> {
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    Validate.validState(bufferedImage.getWidth() == 64, "Must be 64 pixels wide");
                    Validate.validState(bufferedImage.getHeight() == 64, "Must be 64 pixels high");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
                    byte[] bs = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
                    metadata.setFavicon("data:image/png;base64," + new String(bs, StandardCharsets.UTF_8));
                } catch (Exception bufferedImage) {
                    LOGGER.error("Couldn't load server icon", bufferedImage);
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * @param path
     *         relative path from the run directory
     */
    public File getFile(String path) {
        return new File(this.getRunDirectory(), path);
    }

    public File getRunDirectory() {
        return new File(".");
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public static <S extends MinecraftServer> S startServer(Function<Thread, S> serverFactory) {
        AtomicReference<MinecraftServer> atomicReference = new AtomicReference<>();
        Thread thread2 = new Thread(() -> atomicReference.get().runServer(), "Server thread");
        thread2.setUncaughtExceptionHandler((thread, throwable) -> LOGGER.error(throwable));
        if (Runtime.getRuntime().availableProcessors() > 4) {
            thread2.setPriority(8);
        }
        MinecraftServer minecraftServer = serverFactory.apply(thread2);
        atomicReference.set(minecraftServer);
        thread2.start();
        return (S) minecraftServer;
    }

    public abstract void runServer();

    public abstract void tick();

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }

    public boolean shouldPreventProxyConnections() {
        return false;
    }

    public boolean acceptsStatusQuery() {
        return true;
    }

    public ServerMetadata getServerMetadata() {
        setupMetadata(metadata);
        return this.metadata;
    }

    public String getServerMotd() {
        return "";
    }

    public int getCurrentPlayerCount() {
        return 0;
    }

    public int getMaxPlayerCount() {
        return maxPlayers;
    }

    public String getVersion() {
        return SharedConstants.getGameVersion().getName();
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public int getNetworkCompressionThreshold() {
        return compressionThreshold;
    }

    public final ServerWorld getOverworld() {
        return this.worlds.get(World.OVERWORLD);
    }

    @Override
    protected boolean canExecute(ServerTask serverTask) {
        return serverTask.getCreationTicks() + 3 < this.ticks || this.shouldKeepTicking();
    }

    public boolean isOnThread() {
        return Thread.currentThread() == getThread();
    }

    @Override
    public Thread getThread() {
        return this.serverThread;
    }

    public void submitAndJoin(Runnable runnable) {
        if (! this.isOnThread()) {
            this.submitAsync(runnable).join();
        } else {
            runnable.run();
        }
    }

    private CompletableFuture<Void> submitAsync(Runnable runnable) {
        return CompletableFuture.supplyAsync(() -> {
            runnable.run();
            return null;
        }, this);
    }

    private boolean shouldKeepTicking() {
        return this.hasRunningTasks() || TimeUtil.measuringTimeMillions() < (this.waitingForNextTick ? this.nextTickTimestamp : this.timeReference);
    }

    public Set<RegistryKey<World>> getWorldRegistryKeys() {
        return this.worlds.keySet();
    }

    @Nullable
    public ServerWorld getWorld(RegistryKey<World> key) {
        return this.worlds.get(key);
    }

    public Iterable<ServerWorld> getWorlds() {
        return this.worlds.values();
    }

    public String getServerModName() {
        return HYACINTH;
    }
}
