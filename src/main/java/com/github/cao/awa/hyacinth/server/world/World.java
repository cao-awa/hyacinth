package com.github.cao.awa.hyacinth.server.world;

import net.minecraft.util.identifier.Identifier;

public abstract class World {
    private String name;
    private Identifier identifier;
    public static final Identifier OVERWORLD = new Identifier("overworld");
    public static final Identifier NETHER = new Identifier("the_nether");
    public static final Identifier END = new Identifier("the_end");

    public Identifier getIdentifier() {
        return identifier;
    }
}
