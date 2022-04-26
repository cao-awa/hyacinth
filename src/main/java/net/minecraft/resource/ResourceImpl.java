package net.minecraft.resource;

import com.google.gson.*;
import net.minecraft.resource.metadata.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.json.*;
import org.apache.commons.io.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.*;

public class ResourceImpl
implements Resource {
    private final String packName;
    private final Identifier id;
    private final InputStream inputStream;
    private final InputStream metaInputStream;
    private boolean readMetadata;
    private JsonObject metadata;

    public ResourceImpl(String packName, Identifier id, InputStream inputStream, @Nullable InputStream metaInputStream) {
        this.packName = packName;
        this.id = id;
        this.inputStream = inputStream;
        this.metaInputStream = metaInputStream;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public boolean hasMetadata() {
        return this.metaInputStream != null;
    }

    @Override
    @Nullable
    public <T> T getMetadata(ResourceMetadataReader<T> metaReader) {
        Object bufferedReader;
        if (!this.hasMetadata()) {
            return null;
        }
        if (this.metadata == null && !this.readMetadata) {
            this.readMetadata = true;
            bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(this.metaInputStream, StandardCharsets.UTF_8));
                this.metadata = JsonHelper.deserialize((Reader)bufferedReader);
            }
            finally {
                IOUtils.closeQuietly((Reader)bufferedReader);
            }
        }
        if (this.metadata == null) {
            return null;
        }
        bufferedReader = metaReader.getKey();
        return this.metadata.has((String)bufferedReader) ? (T)metaReader.fromJson(JsonHelper.getObject(this.metadata, (String)bufferedReader)) : null;
    }

    @Override
    public String getResourcePackName() {
        return this.packName;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceImpl)) {
            return false;
        }
        ResourceImpl resourceImpl = (ResourceImpl)o;
        if (this.id != null ? !this.id.equals(resourceImpl.id) : resourceImpl.id != null) {
            return false;
        }
        return !(this.packName != null ? !this.packName.equals(resourceImpl.packName) : resourceImpl.packName != null);
    }

    public int hashCode() {
        int i = this.packName != null ? this.packName.hashCode() : 0;
        i = 31 * i + (this.id != null ? this.id.hashCode() : 0);
        return i;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        if (this.metaInputStream != null) {
            this.metaInputStream.close();
        }
    }
}

