package net.minecraft.resource;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.identifier.*;
import net.minecraft.util.json.*;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An abstract implementation of resource reloader that reads JSON files
 * into Gson representations in the prepare stage.
 */
public abstract class JsonDataLoader
extends SinglePreparationResourceReloader<Map<Identifier, JsonElement>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FILE_SUFFIX = ".json";
    private static final int FILE_SUFFIX_LENGTH = ".json".length();
    private final Gson gson;
    private final String dataType;

    public JsonDataLoader(Gson gson, String dataType) {
        this.gson = gson;
        this.dataType = dataType;
    }

    @Override
    protected Map<Identifier, JsonElement> prepare(ResourceManager resourceManager, Profiler profiler) {
        HashMap<Identifier, JsonElement> map = Maps.newHashMap();
        int i = this.dataType.length() + 1;
        for (Identifier identifier : resourceManager.findResources(this.dataType, path -> path.endsWith(FILE_SUFFIX))) {
            String string = identifier.getPath();
            Identifier identifier2 = new Identifier(identifier.getNamespace(), string.substring(i, string.length() - FILE_SUFFIX_LENGTH));
            try {
                Resource resource = resourceManager.getResource(identifier);
                try {
                    InputStream inputStream = resource.getInputStream();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
                        JsonElement jsonElement = JsonHelper.deserialize(this.gson, reader, JsonElement.class);
                        if (jsonElement != null) {
                            JsonElement jsonElement2 = map.put(identifier2, jsonElement);
                            if (jsonElement2 == null) continue;
                            throw new IllegalStateException("Duplicate data file ignored with ID " + identifier2);
                        }
                        LOGGER.error("Couldn't load data file {} from {} as it's null or empty", identifier2, identifier);
                    }
                    finally {
                        if (inputStream == null)
                            continue;
                        inputStream.close();
                    }
                }
                finally {
                    if (resource == null)
                        continue;
                    resource.close();
                }
            }
            catch (JsonParseException | IOException | IllegalArgumentException resource) {
                LOGGER.error("Couldn't parse data file {} from {}", identifier2, identifier, resource);
            }
        }
        return map;
    }
}

