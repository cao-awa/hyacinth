package net.minecraft.resource;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.google.common.collect.*;
import net.minecraft.resource.metadata.*;
import net.minecraft.util.identifier.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class DefaultResourcePack
implements ResourcePack,
ResourceFactory {
    @Nullable
    public static Path resourcePath;
    private static final Logger LOGGER;
    public static Class<?> resourceClass;
    private static final Map<ResourceType, Path> TYPE_TO_FILE_SYSTEM;
    public final PackResourceMetadata metadata;
    public final Set<String> namespaces;

    private static Path getPath(URI uri) throws IOException {
        try {
            return Paths.get(uri);
        }
        catch (FileSystemNotFoundException fileSystemNotFoundException) {
        }
        catch (Throwable throwable) {
            LOGGER.warn("Unable to get path for: {}", uri, throwable);
        }
        try {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
        catch (FileSystemAlreadyExistsException fileSystemAlreadyExistsException) {
            // empty catch block
        }
        return Paths.get(uri);
    }

    public DefaultResourcePack(PackResourceMetadata metadata, String ... namespaces) {
        this.metadata = metadata;
        this.namespaces = ImmutableSet.copyOf(namespaces);
    }

    @Override
    public InputStream openRoot(String fileName) throws IOException {
        Path path;
        if (fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("Root resources can only be filenames, not paths (no / allowed!)");
        }
        if (resourcePath != null && Files.exists(path = resourcePath.resolve(fileName))) {
            return Files.newInputStream(path);
        }
        return this.getInputStream(fileName);
    }

    @Override
    public InputStream open(ResourceType type, Identifier id) throws IOException {
        InputStream inputStream = this.findInputStream(type, id);
        if (inputStream != null) {
            return inputStream;
        }
        throw new FileNotFoundException(id.getPath());
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        Enumeration<?> enumeration2;
        HashSet<Identifier> set = Sets.newHashSet();
        if (resourcePath != null) {
            try {
                DefaultResourcePack.getIdentifiers(set, maxDepth, namespace, resourcePath.resolve(type.getDirectory()), prefix, pathFilter);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (type == ResourceType.CLIENT_RESOURCES) {
                enumeration2 = null;
                try {
                    enumeration2 = resourceClass.getClassLoader().getResources(type.getDirectory() + "/");
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                while (enumeration2 != null && enumeration2.hasMoreElements()) {
                    try {
                        URI uRI = ((URL)enumeration2.nextElement()).toURI();
                        if (!"file".equals(uRI.getScheme())) continue;
                        DefaultResourcePack.getIdentifiers(set, maxDepth, namespace, Paths.get(uRI), prefix, pathFilter);
                    }
                    catch (IOException | URISyntaxException exception) {}
                }
            }
        }
        try {
            Path path = TYPE_TO_FILE_SYSTEM.get(type);
            if (path != null) {
                DefaultResourcePack.getIdentifiers(set, maxDepth, namespace, path, prefix, pathFilter);
            } else {
                LOGGER.error("Can't access assets root for type: {}", type);
            }
        }
        catch (FileNotFoundException | NoSuchFileException ex) {

        }
        catch (IOException enumeration3) {
            LOGGER.error("Couldn't get a list of all vanilla resources", enumeration3);
        }
        return set;
    }

    private static void getIdentifiers(Collection<Identifier> results, int maxDepth, String namespace, Path root, String prefix, Predicate<String> pathFilter) throws IOException {
        Path path2 = root.resolve(namespace);
        try (Stream<Path> stream = Files.walk(path2.resolve(prefix), maxDepth)){
            stream.filter(path -> !path.endsWith(".mcmeta") && Files.isRegularFile(path) && pathFilter.test(path.getFileName().toString())).map(path -> new Identifier(namespace, path2.relativize(path).toString().replaceAll("\\\\", "/"))).forEach(results::add);
        }
    }

    @Nullable
    protected InputStream findInputStream(ResourceType type, Identifier id) {
        Object path;
        String string = DefaultResourcePack.getPath(type, id);
        if (resourcePath != null && Files.exists((Path)(path = resourcePath.resolve(type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath())))) {
            try {
                return Files.newInputStream((Path)path);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        try {
            path = DefaultResourcePack.class.getResource(string);
            if (DefaultResourcePack.isValidUrl(string, (URL)path)) {
                return ((URL)path).openStream();
            }
        }
        catch (IOException path2) {
            return DefaultResourcePack.class.getResourceAsStream(string);
        }
        return null;
    }

    private static String getPath(ResourceType type, Identifier id) {
        return "/" + type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath();
    }

    private static boolean isValidUrl(String fileName, @Nullable URL url) throws IOException {
        return url != null && (url.getProtocol().equals("jar") || DirectoryResourcePack.isValidPath(new File(url.getFile()), fileName));
    }

    @Nullable
    protected InputStream getInputStream(String path) {
        return DefaultResourcePack.class.getResourceAsStream("/" + path);
    }

    @Override
    public boolean contains(ResourceType type, Identifier id) {
        Object path;
        String string = DefaultResourcePack.getPath(type, id);
        if (resourcePath != null && Files.exists((Path)(path = resourcePath.resolve(type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath())))) {
            return true;
        }
        try {
            path = DefaultResourcePack.class.getResource(string);
            return DefaultResourcePack.isValidUrl(string, (URL)path);
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return this.namespaces;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    @Nullable
    public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
        try (InputStream inputStream = this.openRoot("pack.mcmeta")){
            T object;
            if (inputStream != null && (object = AbstractFileResourcePack.parseMetadata(metaReader, inputStream)) != null) {
                T t = object;
                return t;
            }
        }
        catch (FileNotFoundException | RuntimeException exception) {
            // empty catch block
        }
        if (metaReader != PackResourceMetadata.READER) return null;
        return (T)this.metadata;
    }

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public void close() {
    }

    @Override
    public Resource getResource(final Identifier id) throws IOException {
        return new Resource(){
            @Nullable
            InputStream stream;

            @Override
            public void close() throws IOException {
                if (this.stream != null) {
                    this.stream.close();
                }
            }

            @Override
            public Identifier getId() {
                return id;
            }

            @Override
            public InputStream getInputStream() {
                try {
                    this.stream = DefaultResourcePack.this.open(ResourceType.CLIENT_RESOURCES, id);
                }
                catch (IOException iOException) {
                    throw new UncheckedIOException("Could not get client resource from vanilla pack", iOException);
                }
                return this.stream;
            }

            @Override
            public boolean hasMetadata() {
                return false;
            }

            @Override
            @Nullable
            public <T> T getMetadata(ResourceMetadataReader<T> metaReader) {
                return null;
            }

            @Override
            public String getResourcePackName() {
                return id.toString();
            }
        };
    }

    static {
        LOGGER = LogManager.getLogger();
        TYPE_TO_FILE_SYSTEM = EntrustParser.operation(() -> {
            synchronized (DefaultResourcePack.class) {
                ImmutableMap.Builder<ResourceType, Path> builder = ImmutableMap.builder();
                for (ResourceType resourceType : ResourceType.values()) {
                    String string = "/" + resourceType.getDirectory() + "/.mcassetsroot";
                    URL uRL = DefaultResourcePack.class.getResource(string);
                    if (uRL == null) {
                        LOGGER.error("File {} does not exist in classpath", string);
                        continue;
                    }
                    try {
                        URI uRI = uRL.toURI();
                        String string2 = uRI.getScheme();
                        if (!"jar".equals(string2) && !"file".equals(string2)) {
                            LOGGER.warn("Assets URL '{}' uses unexpected schema", uRI);
                        }
                        Path path = DefaultResourcePack.getPath(uRI);
                        builder.put(resourceType, path.getParent());
                    }
                    catch (Exception uRI) {
                        LOGGER.error("Couldn't resolve path to vanilla assets", uRI);
                    }
                }
                return builder.build();
            }
        });
    }
}

