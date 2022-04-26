package net.minecraft.resource.metadata;

import com.github.cao.awa.hyacinth.network.text.*;

public class PackResourceMetadata {
    public static final PackResourceMetadataReader READER = new PackResourceMetadataReader();
    private final Text description;
    private final int packFormat;

    public PackResourceMetadata(Text description, int format) {
        this.description = description;
        this.packFormat = format;
    }

    public Text getDescription() {
        return this.description;
    }

    public int getPackFormat() {
        return this.packFormat;
    }
}

