package com.github.cao.awa.hyacinth.server.dimension;

import com.github.cao.awa.hyacinth.math.*;
import com.github.cao.awa.hyacinth.math.block.*;
import com.github.cao.awa.hyacinth.server.world.*;
import com.mojang.datafixers.kinds.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.tag.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.registry.*;

import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class DimensionType {
    public static final int SIZE_BITS_Y = BlockPos.SIZE_BITS_Y;
    public static final int field_33411 = 16;
    public static final int MAX_HEIGHT = (1 << SIZE_BITS_Y) - 32;
    public static final int MAX_COLUMN_HEIGHT = (MAX_HEIGHT >> 1) - 1;
    public static final int MIN_HEIGHT = MAX_COLUMN_HEIGHT - MAX_HEIGHT + 1;
    public static final int field_35478 = MAX_COLUMN_HEIGHT << 4;
    public static final int field_35479 = MIN_HEIGHT << 4;
    public static final Identifier OVERWORLD_ID = new Identifier("overworld");
    public static final Identifier THE_NETHER_ID = new Identifier("the_nether");
    public static final Identifier THE_END_ID = new Identifier("the_end");
    public static final Codec<DimensionType> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.optionalFieldOf("fixed_time").xmap(
                    optional -> optional.map(OptionalLong::of).orElseGet(OptionalLong::empty),
                    optionalLong -> optionalLong.isPresent() ?
                            Optional.of(optionalLong.getAsLong()) :
                            Optional.empty()).forGetter(
                    dimensionType -> ((DimensionType) dimensionType).fixedTime),
            (Codec.BOOL.fieldOf("has_skylight")).forGetter(
                    t -> ((DimensionType) t).hasSkyLight()),
            (Codec.BOOL.fieldOf("has_ceiling")).forGetter(
                    t1 -> ((DimensionType) t1).hasCeiling()),
            (Codec.BOOL.fieldOf("ultrawarm")).forGetter(
                    t2 -> ((DimensionType) t2).isUltrawarm()),
            (Codec.BOOL.fieldOf("natural")).forGetter(
                    t3 -> ((DimensionType) t3).isNatural()),
            (Codec.doubleRange(1.0E-5f, 3.0E7).fieldOf("coordinate_scale")).forGetter(
                    t4 -> ((DimensionType) t4).getCoordinateScale()),
            (Codec.BOOL.fieldOf("piglin_safe")).forGetter(
                    t5 -> ((DimensionType) t5).isPiglinSafe()),
            (Codec.BOOL.fieldOf("bed_works")).forGetter(
                    t6 -> ((DimensionType) t6).isBedWorking()),
            (Codec.BOOL.fieldOf("respawn_anchor_works")).forGetter(
                    t7 -> ((DimensionType) t7).isRespawnAnchorWorking()),
            (Codec.BOOL.fieldOf("has_raids")).forGetter(
                    t8 -> ((DimensionType) t8).hasRaids()),
            (Codec.intRange(MIN_HEIGHT, MAX_COLUMN_HEIGHT).fieldOf("min_y")).forGetter(
                    t9 -> ((DimensionType) t9).getMinimumY()),
            (Codec.intRange(16, MAX_HEIGHT).fieldOf("height")).forGetter(
                    t10 -> ((DimensionType) t10).getHeight()),
            (Codec.intRange(0, MAX_HEIGHT).fieldOf("logical_height")).forGetter(
                    t11 -> ((DimensionType) t11).getLogicalHeight()),
            (Identifier.CODEC.fieldOf("infiniburn")).forGetter(
                    dimensionType -> ((DimensionType) dimensionType).infiniburn),
            (Identifier.CODEC.fieldOf("effects")).orElse(OVERWORLD_ID).forGetter(
                    dimensionType -> ((DimensionType) dimensionType).effects),
            (Codec.FLOAT.fieldOf("ambient_light")).forGetter(
                    dimensionType -> ((DimensionType) dimensionType).ambientLight)
    ).apply(instance, DimensionType::new)).comapFlatMap(type -> checkHeight(((DimensionType)type)), Function.identity());
    private static final int field_31440 = 8;
    public static final float[] MOON_SIZES = new float[]{1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f};
    public static final RegistryKey<DimensionType> OVERWORLD_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("overworld"));
    public static final RegistryKey<DimensionType> THE_NETHER_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("the_nether"));
    public static final RegistryKey<DimensionType> THE_END_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("the_end"));
    public static final DimensionType OVERWORLD = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD.getId(), OVERWORLD_ID, 0.0f);
    public static final DimensionType THE_NETHER = DimensionType.create(OptionalLong.of(18000L), false, true, true, false, 8.0, false, true, false, true, false, 0, 256, 128, BlockTags.INFINIBURN_NETHER.getId(), THE_NETHER_ID, 0.1f);
    public static final DimensionType THE_END = DimensionType.create(OptionalLong.of(6000L), false, false, false, false, 1.0, true, false, false, false, true, 0, 256, 256, BlockTags.INFINIBURN_END.getId(), THE_END_ID, 0.0f);
    public static final RegistryKey<DimensionType> OVERWORLD_CAVES_REGISTRY_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("overworld_caves"));
    protected static final DimensionType OVERWORLD_CAVES = DimensionType.create(OptionalLong.empty(), true, true, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD.getId(), OVERWORLD_ID, 0.0f);
    public static final Codec<Supplier<DimensionType>> REGISTRY_CODEC = RegistryElementCodec.of(Registry.DIMENSION_TYPE_KEY, CODEC);
    private final OptionalLong fixedTime;
    private final boolean hasSkyLight;
    private final boolean hasCeiling;
    private final boolean ultrawarm;
    private final boolean natural;
    private final double coordinateScale;
    private final boolean hasEnderDragonFight;
    private final boolean piglinSafe;
    private final boolean bedWorks;
    private final boolean respawnAnchorWorks;
    private final boolean hasRaids;
    private final int minimumY;
    private final int height;
    private final int logicalHeight;
    private final Identifier infiniburn;
    private final Identifier effects;
    private final float ambientLight;
    private final transient float[] brightnessByLightLevel;

    private static DataResult<DimensionType> checkHeight(DimensionType type) {
        if (type.getHeight() < 16) {
            return DataResult.error("height has to be at least 16");
        }
        if (type.getMinimumY() + type.getHeight() > MAX_COLUMN_HEIGHT + 1) {
            return DataResult.error("min_y + height cannot be higher than: " + (MAX_COLUMN_HEIGHT + 1));
        }
        if (type.getLogicalHeight() > type.getHeight()) {
            return DataResult.error("logical_height cannot be higher than height");
        }
        if (type.getHeight() % 16 != 0) {
            return DataResult.error("height has to be multiple of 16");
        }
        if (type.getMinimumY() % 16 != 0) {
            return DataResult.error("min_y has to be a multiple of 16");
        }
        return DataResult.success(type);
    }

    private DimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int minimumY, int height, int logicalHeight, Identifier infiniburn, Identifier effects, float ambientLight) {
        this(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, coordinateScale, false, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, minimumY, height, logicalHeight, infiniburn, effects, ambientLight);
    }

    public static DimensionType create(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int minimumY, int height, int logicalHeight, Identifier infiniburn, Identifier effects, float ambientLight) {
        DimensionType dimensionType = new DimensionType(fixedTime, hasSkylight, hasCeiling, ultrawarm, natural, coordinateScale, hasEnderDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, minimumY, height, logicalHeight, infiniburn, effects, ambientLight);
        DimensionType.checkHeight(dimensionType).error().ifPresent(partialResult -> {
            throw new IllegalStateException(partialResult.message());
        });
        return dimensionType;
    }

    @Deprecated
    private DimensionType(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int minimumY, int height, int logicalHeight, Identifier infiniburn, Identifier effects, float ambientLight) {
        this.fixedTime = fixedTime;
        this.hasSkyLight = hasSkylight;
        this.hasCeiling = hasCeiling;
        this.ultrawarm = ultrawarm;
        this.natural = natural;
        this.coordinateScale = coordinateScale;
        this.hasEnderDragonFight = hasEnderDragonFight;
        this.piglinSafe = piglinSafe;
        this.bedWorks = bedWorks;
        this.respawnAnchorWorks = respawnAnchorWorks;
        this.hasRaids = hasRaids;
        this.minimumY = minimumY;
        this.height = height;
        this.logicalHeight = logicalHeight;
        this.infiniburn = infiniburn;
        this.effects = effects;
        this.ambientLight = ambientLight;
        this.brightnessByLightLevel = DimensionType.computeBrightnessByLightLevel(ambientLight);
    }

    private static float[] computeBrightnessByLightLevel(float ambientLight) {
        float[] fs = new float[16];
        for (int i = 0; i <= 15; ++i) {
            float f = (float)i / 15.0f;
            float g = f / (4.0f - 3.0f * f);
            fs[i] = Mathematics.lerp(ambientLight, g, 1.0f);
        }
        return fs;
    }

    @Deprecated
    public static DataResult<RegistryKey<World>> worldFromDimensionNbt(Dynamic<?> nbt) {
        Optional<Number> optional = nbt.asNumber().result();
        if (optional.isPresent()) {
            int i = optional.get().intValue();
            if (i == -1) {
                return DataResult.success(World.NETHER);
            }
            if (i == 0) {
                return DataResult.success(World.OVERWORLD);
            }
            if (i == 1) {
                return DataResult.success(World.END);
            }
        }
        return World.CODEC.parse(nbt);
    }

    public static DynamicRegistryManager addRegistryDefaults(DynamicRegistryManager registryManager) {
        MutableRegistry<DimensionType> mutableRegistry = registryManager.getMutable(Registry.DIMENSION_TYPE_KEY);
        mutableRegistry.add(OVERWORLD_REGISTRY_KEY, OVERWORLD, Lifecycle.stable());
        mutableRegistry.add(OVERWORLD_CAVES_REGISTRY_KEY, OVERWORLD_CAVES, Lifecycle.stable());
        mutableRegistry.add(THE_NETHER_REGISTRY_KEY, THE_NETHER, Lifecycle.stable());
        mutableRegistry.add(THE_END_REGISTRY_KEY, THE_END, Lifecycle.stable());
        return registryManager;
    }

//    public static SimpleRegistry<DimensionOptions> createDefaultDimensionOptions(DynamicRegistryManager registryManager, long seed) {
//        return DimensionType.createDefaultDimensionOptions(registryManager, seed, true);
//    }

//    public static SimpleRegistry<DimensionOptions> createDefaultDimensionOptions(DynamicRegistryManager registryManager, long seed, boolean bl) {
//        SimpleRegistry<DimensionOptions> simpleRegistry = new SimpleRegistry<DimensionOptions>(Registry.DIMENSION_KEY, Lifecycle.experimental());
//        Registry<DimensionType> registry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
//        Registry<Biome> registry2 = registryManager.get(Registry.BIOME_KEY);
//        Registry<ChunkGeneratorSettings> registry3 = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
//        Registry<DoublePerlinNoiseSampler.NoiseParameters> registry4 = registryManager.get(Registry.NOISE_WORLDGEN);
//        simpleRegistry.add(DimensionOptions.NETHER, new DimensionOptions(() -> registry.getOrThrow(THE_NETHER_REGISTRY_KEY), new NoiseChunkGenerator(registry4, (BiomeSource)MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(registry2, bl), seed, () -> registry3.getOrThrow(ChunkGeneratorSettings.NETHER))), Lifecycle.stable());
//        simpleRegistry.add(DimensionOptions.END, new DimensionOptions(() -> registry.getOrThrow(THE_END_REGISTRY_KEY), new NoiseChunkGenerator(registry4, (BiomeSource)new TheEndBiomeSource(registry2, seed), seed, () -> registry3.getOrThrow(ChunkGeneratorSettings.END))), Lifecycle.stable());
//        return simpleRegistry;
//    }

    public static double getCoordinateScaleFactor(DimensionType fromDimension, DimensionType toDimension) {
        double d = fromDimension.getCoordinateScale();
        double e = toDimension.getCoordinateScale();
        return d / e;
    }

    @Deprecated
    public String getSuffix() {
        if (this.equals(THE_END)) {
            return "_end";
        }
        return "";
    }

    public static Path getSaveDirectory(RegistryKey<World> worldRef, Path worldDirectory) {
        if (worldRef == World.OVERWORLD) {
            return worldDirectory;
        }
        if (worldRef == World.END) {
            return worldDirectory.resolve("DIM1");
        }
        if (worldRef == World.NETHER) {
            return worldDirectory.resolve("DIM-1");
        }
        return worldDirectory.resolve("dimensions").resolve(worldRef.getValue().getNamespace()).resolve(worldRef.getValue().getPath());
    }

    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }

    public boolean hasCeiling() {
        return this.hasCeiling;
    }

    public boolean isUltrawarm() {
        return this.ultrawarm;
    }

    public boolean isNatural() {
        return this.natural;
    }

    public double getCoordinateScale() {
        return this.coordinateScale;
    }

    public boolean isPiglinSafe() {
        return this.piglinSafe;
    }

    public boolean isBedWorking() {
        return this.bedWorks;
    }

    public boolean isRespawnAnchorWorking() {
        return this.respawnAnchorWorks;
    }

    public boolean hasRaids() {
        return this.hasRaids;
    }

    public int getMinimumY() {
        return this.minimumY;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLogicalHeight() {
        return this.logicalHeight;
    }

    public boolean hasEnderDragonFight() {
        return this.hasEnderDragonFight;
    }

    public boolean hasFixedTime() {
        return this.fixedTime.isPresent();
    }

    public float getSkyAngle(long time) {
        double d = Mathematics.fractionalPart((double)this.fixedTime.orElse(time) / 24000.0 - 0.25);
        double e = 0.5 - Math.cos(d * Math.PI) / 2.0;
        return (float)(d * 2.0 + e) / 3.0f;
    }

    /**
     * Gets the moon phase index of Minecraft's moon.
     *
     * <p>This is typically used to determine the size of the moon that should be rendered.
     *
     * @param time the time to calculate the index from
     */
    public int getMoonPhase(long time) {
        return (int)(time / 24000L % 8L + 8L) % 8;
    }

    public float getBrightness(int lightLevel) {
        return this.brightnessByLightLevel[lightLevel];
    }

//    public Tag<Block> getInfiniburnBlocks() {
//        Tag<Block> tag = BlockTags.getTagGroup().getTag(this.infiniburn);
//        return tag != null ? tag : BlockTags.INFINIBURN_OVERWORLD;
//    }

    /**
     * {@return the ID of this dimension's {@linkplain net.minecraft.client.render.DimensionEffects effects}}
     *
     * @see net.minecraft.client.render.DimensionEffects#byDimensionType(DimensionType)
     */
    public Identifier getEffects() {
        return this.effects;
    }

    public boolean equals(DimensionType dimensionType) {
        if (this == dimensionType) {
            return true;
        }
        return this.hasSkyLight == dimensionType.hasSkyLight && this.hasCeiling == dimensionType.hasCeiling && this.ultrawarm == dimensionType.ultrawarm && this.natural == dimensionType.natural && this.coordinateScale == dimensionType.coordinateScale && this.hasEnderDragonFight == dimensionType.hasEnderDragonFight && this.piglinSafe == dimensionType.piglinSafe && this.bedWorks == dimensionType.bedWorks && this.respawnAnchorWorks == dimensionType.respawnAnchorWorks && this.hasRaids == dimensionType.hasRaids && this.minimumY == dimensionType.minimumY && this.height == dimensionType.height && this.logicalHeight == dimensionType.logicalHeight && Float.compare(dimensionType.ambientLight, this.ambientLight) == 0 && this.fixedTime.equals(dimensionType.fixedTime) && this.infiniburn.equals(dimensionType.infiniburn) && this.effects.equals(dimensionType.effects);
    }
}

