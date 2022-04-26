package net.minecraft.resource;

import com.google.common.base.*;
import com.google.common.collect.*;
import net.minecraft.util.identifier.*;
import org.apache.commons.io.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.*;

public class ZipResourcePack
extends AbstractFileResourcePack {
    public static final Splitter TYPE_NAMESPACE_SPLITTER = Splitter.on('/').omitEmptyStrings().limit(3);
    @Nullable
    private ZipFile file;

    public ZipResourcePack(File file) {
        super(file);
    }

    private ZipFile getZipFile() throws IOException {
        if (this.file == null) {
            this.file = new ZipFile(this.base);
        }
        return this.file;
    }

    @Override
    protected InputStream openFile(String name) throws IOException {
        ZipFile zipFile = this.getZipFile();
        ZipEntry zipEntry = zipFile.getEntry(name);
        if (zipEntry == null) {
            throw new ResourceNotFoundException(this.base, name);
        }
        return zipFile.getInputStream(zipEntry);
    }

    @Override
    public boolean containsFile(String name) {
        try {
            return this.getZipFile().getEntry(name) != null;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        ZipFile zipFile;
        try {
            zipFile = this.getZipFile();
        }
        catch (IOException iOException) {
            return Collections.emptySet();
        }
        Enumeration<? extends ZipEntry> iOException = zipFile.entries();
        HashSet<String> set = Sets.newHashSet();
        while (iOException.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipEntry = iOException.nextElement();
            String string = zipEntry.getName();
            if (!string.startsWith(type.getDirectory() + "/") || (list = Lists.newArrayList(TYPE_NAMESPACE_SPLITTER.split(string))).size() <= 1) continue;
            String string2 = list.get(1);
            if (string2.equals(string2.toLowerCase(Locale.ROOT))) {
                set.add(string2);
                continue;
            }
            this.warnNonLowerCaseNamespace(string2);
        }
        return set;
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void close() {
        if (this.file != null) {
            IOUtils.closeQuietly(this.file);
            this.file = null;
        }
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        ZipFile zipFile;
        try {
            zipFile = this.getZipFile();
        }
        catch (IOException iOException) {
            return Collections.emptySet();
        }
        Enumeration<? extends ZipEntry> iOException = zipFile.entries();
        ArrayList<Identifier> list = Lists.newArrayList();
        String string = type.getDirectory() + "/" + namespace + "/";
        String string2 = string + prefix + "/";
        while (iOException.hasMoreElements()) {
            String string4;
            String[] strings;
            String string3;
            ZipEntry zipEntry = iOException.nextElement();
            if (zipEntry.isDirectory() || (string3 = zipEntry.getName()).endsWith(".mcmeta") || !string3.startsWith(string2) || (strings = (string4 = string3.substring(string.length())).split("/")).length < maxDepth + 1 || !pathFilter.test(strings[strings.length - 1]))
                continue;
            list.add(new Identifier(namespace, string4));
        }
        return list;
    }
}

