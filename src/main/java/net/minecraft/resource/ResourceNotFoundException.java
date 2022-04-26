/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package net.minecraft.resource;

import java.io.File;
import java.io.FileNotFoundException;

public class ResourceNotFoundException
extends FileNotFoundException {
    public ResourceNotFoundException(File packSource, String resource) {
        super(String.format("'%s' in ResourcePack '%s'", resource, packSource));
    }
}

