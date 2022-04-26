package com.github.cao.awa.hyacinth.server.world.chunk;

import com.github.cao.awa.hyacinth.server.world.*;
import com.github.cao.awa.hyacinth.server.world.level.storage.*;
import net.minecraft.util.thread.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class ServerChunkManager {
    private static final Logger LOGGER = LogManager.getLogger("Manager:Chunk");
    final ServerWorld world;
    final Thread serverThread;
    private final MainThreadExecutor mainThreadExecutor;
    private long lastMobSpawningTime;
    private boolean spawnMonsters = true;
    private boolean spawnAnimals = true;
    private static final int field_29766 = 4;
    private final long[] chunkPosCache = new long[4];

//    public ServerChunkManager(ServerWorld world, LevelStorage.Session session, StructureManager structureManager, Executor workerExecutor, ChunkGenerator chunkGenerator, int viewDistance, int simulationDistance, boolean dsync, WorldGenerationProgressListener worldGenerationProgressListener, ChunkStatusChangeListener chunkStatusChangeListener, Supplier<PersistentStateManager> persistentStateManagerFactory) {
//        this.world = world;
//        this.mainThreadExecutor = new MainThreadExecutor(world);
//        this.serverThread = Thread.currentThread();
//        File file = session.getWorldDirectory(world.getRegistryKey()).resolve("data").toFile();
//        file.mkdirs();
//        this.persistentStateManager = new PersistentStateManager(file, dataFixer);
//        this.threadedAnvilChunkStorage = new ThreadedAnvilChunkStorage(world, session, dataFixer, structureManager, workerExecutor, this.mainThreadExecutor, this, chunkGenerator, worldGenerationProgressListener, chunkStatusChangeListener, persistentStateManagerFactory, viewDistance, dsync);
//        this.lightingProvider = this.threadedAnvilChunkStorage.getLightingProvider();
//        this.ticketManager = this.threadedAnvilChunkStorage.getTicketManager();
//        this.ticketManager.setSimulationDistance(simulationDistance);
//        this.initChunkCaches();
//    }

    public ServerChunkManager(ServerWorld world, LevelStorage.Session session, Executor workerExecutor,int viewDistance, int simulationDistance, boolean dsync) {
        this.mainThreadExecutor = new MainThreadExecutor(world);
        this.world = world;
        this.serverThread = Thread.currentThread();
    }

    public boolean tick() {
//        boolean bl = this.ticketManager.tick(this.threadedAnvilChunkStorage);
//        boolean bl2 = this.threadedAnvilChunkStorage.updateHolderMap();
//        if (bl || bl2) {
//            this.initChunkCaches();
//            return true;
//        }
        return false;
    }

    final class MainThreadExecutor extends ThreadExecutor<Runnable> {
        MainThreadExecutor(World world) {
            super("Chunk source main thread executor for " + world.getRegistryKey().getValue());
        }

        @Override
        protected Runnable createTask(Runnable runnable) {
            return runnable;
        }

        @Override
        protected boolean canExecute(Runnable task) {
            return true;
        }

        @Override
        protected boolean shouldExecuteAsync() {
            return true;
        }

        @Override
        protected Thread getThread() {
            return ServerChunkManager.this.serverThread;
        }

        @Override
        protected void executeTask(Runnable task) {
//            ServerChunkManager.this.world.getProfiler().visit("runTask");
            super.executeTask(task);
        }

        @Override
        public boolean runTask() {
            if (ServerChunkManager.this.tick()) {
                return true;
            }
//            ServerChunkManager.this.lightingProvider.tick();
            return super.runTask();
        }
    }
}
