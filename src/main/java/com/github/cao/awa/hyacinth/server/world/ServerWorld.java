package com.github.cao.awa.hyacinth.server.world;

import com.github.cao.awa.hyacinth.math.block.*;
import com.github.cao.awa.hyacinth.server.*;
import com.github.cao.awa.hyacinth.server.dimension.*;
import com.github.cao.awa.hyacinth.server.world.chunk.*;
import com.github.cao.awa.hyacinth.server.world.level.storage.*;
import net.minecraft.util.registry.*;

import java.util.*;
import java.util.concurrent.*;

public class ServerWorld extends World {
    private final ServerChunkManager chunkManager;
    private final MinecraftServer server;

    public ServerWorld(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, RegistryKey<World> worldKey, DimensionType dimensionType, long seed, boolean shouldTickTime) {
        super(worldKey, dimensionType);
        this.server = server;
        this.chunkManager = new ServerChunkManager(this,session, workerExecutor, server.getPlayerManager().getViewDistance(), server.getPlayerManager().getSimulationDistance(), false);
    }

    public BlockPos getSpawnPos() {
//        BlockPos blockPos = new BlockPos(this.properties.getSpawnX(), this.properties.getSpawnY(), this.properties.getSpawnZ());
//        if (! this.getWorldBorder().contains(blockPos)) {
//            blockPos = this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
//        }
        return new BlockPos(0,100,0);
    }

    public float getSpawnAngle() {
//        return this.properties.getSpawnAngle();
        return 0;
    }
}
