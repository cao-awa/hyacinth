/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.util.collection;

import com.github.cao.awa.hyacinth.constants.SharedConstants;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustExecution;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Weight {
    public static final Codec<Weight> CODEC = Codec.INT.xmap(Weight::of, Weight::getValue);
    private static final Weight DEFAULT = new Weight(1);
    private static final Logger LOGGER = LogManager.getLogger();
    private final int value;

    private Weight(int weight) {
        this.value = weight;
    }

    public static Weight of(int weight) {
        if (weight == 1) {
            return DEFAULT;
        }
        Weight.validate(weight);
        return new Weight(weight);
    }

    public int getValue() {
        return this.value;
    }

    private static void validate(int weight) {
        if (weight < 0) {
            throw EntrustExecution.throwOrPause(new IllegalArgumentException("Weight should be >= 0"));
        }
        if (weight == 0 && SharedConstants.isDevelopment) {
            LOGGER.warn("Found 0 weight, make sure this is intentional!");
        }
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    public int hashCode() {
        return Integer.hashCode(this.value);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Weight && this.value == ((Weight)o).value;
    }
}

