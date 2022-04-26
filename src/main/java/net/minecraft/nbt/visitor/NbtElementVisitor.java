package net.minecraft.nbt.visitor;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtByteArray;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.nbt.NbtLongArray;
import net.minecraft.nbt.NbtNull;
import net.minecraft.nbt.NbtShort;
import net.minecraft.nbt.NbtString;

/**
 * A visitor interface for NBT elements.
 */
public interface NbtElementVisitor {
    void visitString(NbtString var1);

    void visitByte(NbtByte var1);

    void visitShort(NbtShort var1);

    void visitInt(NbtInt var1);

    void visitLong(NbtLong var1);

    void visitFloat(NbtFloat var1);

    void visitDouble(NbtDouble var1);

    void visitByteArray(NbtByteArray var1);

    void visitIntArray(NbtIntArray var1);

    void visitLongArray(NbtLongArray var1);

    void visitList(NbtList var1);

    void visitCompound(NbtCompound var1);

    void visitNull(NbtNull var1);
}

