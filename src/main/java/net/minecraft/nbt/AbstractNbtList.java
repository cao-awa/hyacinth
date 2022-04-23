package net.minecraft.nbt;

import java.util.AbstractList;
import net.minecraft.nbt.NbtElement;

/**
 * Represents an abstraction of a mutable NBT list which holds elements of the same type.
 */
public abstract class AbstractNbtList<T extends NbtElement>
extends AbstractList<T>
implements NbtElement {
    @Override
    public abstract T set(int var1, T var2);

    @Override
    public abstract void add(int var1, T var2);

    @Override
    public abstract T remove(int var1);

    public abstract boolean setElement(int var1, NbtElement var2);

    public abstract boolean addElement(int var1, NbtElement var2);

    /**
     * Gets the {@linkplain NbtElement#getType type} of element that this list holds.
     * 
     * @return the type of element that this list holds
     */
    public abstract byte getHeldType();
}

