package net.minecraft.resource;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public class DataPackSettings {
    public static final DataPackSettings SAFE_MODE = new DataPackSettings(ImmutableList.of("vanilla"), ImmutableList.of());
    public static final Codec<DataPackSettings> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(((MapCodec) Codec.STRING.listOf().fieldOf("Enabled")).forGetter(dataPackSettings -> ((DataPackSettings) dataPackSettings).enabled), ((MapCodec) Codec.STRING.listOf().fieldOf("Disabled")).forGetter(dataPackSettings -> ((DataPackSettings) dataPackSettings).disabled)).apply(instance, (enabled1, disabled1) -> new DataPackSettings((List<String>) enabled1, (List<String>) disabled1));
    });
    private final List<String> enabled;
    private final List<String> disabled;

    public DataPackSettings(List<String> enabled, List<String> disabled) {
        this.enabled = ImmutableList.copyOf(enabled);
        this.disabled = ImmutableList.copyOf(disabled);
    }

    public List<String> getEnabled() {
        return this.enabled;
    }

    public List<String> getDisabled() {
        return this.disabled;
    }
}

