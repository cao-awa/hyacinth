package com.github.cao.awa.hyacinth.server.world;

import com.github.cao.awa.hyacinth.server.dimension.*;
import com.mojang.serialization.*;
import net.minecraft.util.identifier.Identifier;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;

import java.util.function.*;

public abstract class World {
    private String name;
    private Identifier identifier;
    public static final Codec<RegistryKey<World>> CODEC = Identifier.CODEC.xmap(RegistryKey.createKeyFactory(Registry.WORLD_KEY), RegistryKey::getValue);
    public static final RegistryKey<World> OVERWORLD = RegistryKey.of(Registry.WORLD_KEY, new Identifier("overworld"));
    public static final RegistryKey<World> NETHER = RegistryKey.of(Registry.WORLD_KEY, new Identifier("the_nether"));
    public static final RegistryKey<World> END = RegistryKey.of(Registry.WORLD_KEY, new Identifier("the_end"));
    public final RegistryKey<World> registryKey;
    private final DimensionType dimension;

//    protected World(MutableWorldProperties properties, RegistryKey<World> registryRef, final DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
//        this.profiler = profiler;
//        this.properties = properties;
//        this.dimension = dimensionType;
//        this.registryKey = registryRef;
//        this.isClient = isClient;
//        this.border = dimensionType.getCoordinateScale() != 1.0 ? new WorldBorder(){
//            @Override
//            public double getCenterX() {
//                return super.getCenterX() / dimensionType.getCoordinateScale();
//            }
//
//            @Override
//            public double getCenterZ() {
//                return super.getCenterZ() / dimensionType.getCoordinateScale();
//            }
//        } : new WorldBorder();
//        this.thread = Thread.currentThread();
//        this.biomeAccess = new BiomeAccess(this, seed);
//        this.debugWorld = debugWorld;
//    }

    public World(RegistryKey<World> registryRef, final DimensionType dimension) {
        this.registryKey = registryRef;
        this.dimension = dimension;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public DimensionType getDimension() {
        return this.dimension;
    }

    public RegistryKey<World> getRegistryKey() {
        return this.registryKey;
    }
}
