package net.minecraft.resource;

import com.github.cao.awa.hyacinth.network.text.*;
import com.github.cao.awa.hyacinth.network.text.style.*;
import com.github.cao.awa.hyacinth.network.text.translate.*;

public interface ResourcePackSource {
    ResourcePackSource PACK_SOURCE_NONE = ResourcePackSource.onlyName();
    ResourcePackSource PACK_SOURCE_BUILTIN = ResourcePackSource.nameAndSource("pack.source.builtin");
    ResourcePackSource PACK_SOURCE_WORLD = ResourcePackSource.nameAndSource("pack.source.world");
    ResourcePackSource PACK_SOURCE_SERVER = ResourcePackSource.nameAndSource("pack.source.server");

    Text decorate(Text var1);

    static ResourcePackSource onlyName() {
        return name -> name;
    }

    static ResourcePackSource nameAndSource(String source) {
        TranslatableText text = new TranslatableText(source);
        return name -> new TranslatableText("pack.nameAndSource", name, text).formatted(Formatting.GRAY);
    }
}

