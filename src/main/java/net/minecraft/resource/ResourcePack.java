package net.minecraft.resource;

import net.minecraft.resource.metadata.*;
import net.minecraft.util.identifier.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * A resource pack, providing resources to resource managers.
 * 
 * <p>They are single-use in each reload cycle of a reloadable resource manager.
 * {@link ResourcePackProfile} is a persistent version of the resource packs.
 */
public interface ResourcePack
extends AutoCloseable {
    String METADATA_PATH_SUFFIX = ".mcmeta";
    String PACK_METADATA_NAME = "pack.mcmeta";

    @Nullable InputStream openRoot(String var1) throws IOException;

    InputStream open(ResourceType var1, Identifier var2) throws IOException;

    Collection<Identifier> findResources(ResourceType var1, String var2, String var3, int var4, Predicate<String> var5);

    boolean contains(ResourceType var1, Identifier var2);

    Set<String> getNamespaces(ResourceType var1);

    @Nullable <T> T parseMetadata(ResourceMetadataReader<T> var1) throws IOException;

    String getName();

    @Override
    void close();
}

