package net.minecraft.resource;

import com.google.gson.*;
import net.minecraft.resource.metadata.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.json.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.*;

public abstract class AbstractFileResourcePack
implements ResourcePack {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final File base;

    public AbstractFileResourcePack(File base) {
        this.base = base;
    }

    private static String getFilename(net.minecraft.resource.ResourceType type, Identifier id) {
        return String.format("%s/%s/%s", type.getDirectory(), id.getNamespace(), id.getPath());
    }

    protected static String relativize(File base, File target) {
        return base.toURI().relativize(target.toURI()).getPath();
    }

    @Override
    public InputStream open(net.minecraft.resource.ResourceType type, Identifier id) throws IOException {
        return this.openFile(AbstractFileResourcePack.getFilename(type, id));
    }

    @Override
    public boolean contains(net.minecraft.resource.ResourceType type, Identifier id) {
        return this.containsFile(AbstractFileResourcePack.getFilename(type, id));
    }

    protected abstract InputStream openFile(String var1) throws IOException;

    @Override
    public InputStream openRoot(String fileName) throws IOException {
        if (fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        return this.openFile(fileName);
    }

    protected abstract boolean containsFile(String var1);

    protected void warnNonLowerCaseNamespace(String namespace) {
        LOGGER.warn("ResourcePack: ignored non-lowercase namespace: {} in {}", namespace, this.base);
    }

    @Override
    @Nullable
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        try (InputStream inputStream = this.openFile("pack.mcmeta")){
            return AbstractFileResourcePack.parseMetadata(metaReader, inputStream);
        }
    }

    @Nullable
    public static <T> T parseMetadata(net.minecraft.resource.metadata.ResourceMetadataReader<T> metaReader, InputStream inputStream) {
        JsonObject jsonObject;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            jsonObject = JsonHelper.deserialize(bufferedReader);
        }
        catch (JsonParseException | IOException bufferedReader2) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), bufferedReader2);
            return null;
        }
        if (!jsonObject.has(metaReader.getKey())) {
            return null;
        }
        try {
            return metaReader.fromJson(JsonHelper.getObject(jsonObject, metaReader.getKey()));
        }
        catch (JsonParseException bufferedReader) {
            LOGGER.error("Couldn't load {} metadata", metaReader.getKey(), bufferedReader);
            return null;
        }
    }

    @Override
    public String getName() {
        return this.base.getName();
    }
}

