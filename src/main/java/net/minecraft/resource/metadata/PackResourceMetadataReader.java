package net.minecraft.resource.metadata;

import com.github.cao.awa.hyacinth.network.text.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.json.*;

public class PackResourceMetadataReader
implements ResourceMetadataReader<PackResourceMetadata> {
    @Override
    public PackResourceMetadata fromJson(JsonObject jsonObject) {
        MutableText text = Text.Serializer.fromJson(jsonObject.get("description"));
        if (text == null) {
            throw new JsonParseException("Invalid/missing description!");
        }
        int i = JsonHelper.getInt(jsonObject, "pack_format");
        return new PackResourceMetadata(text, i);
    }

    @Override
    public String getKey() {
        return "pack";
    }
}

