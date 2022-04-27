//package com.github.cao.awa.hyacinth.server.inventory;
//
//import com.github.cao.awa.hyacinth.server.entity.player.*;
//import com.github.cao.awa.hyacinth.server.item.*;
//import net.minecraft.util.*;
//
//public interface Inventory
//        extends Clearable {
//    int MAX_COUNT_PER_STACK = 64;
//
//    int size();
//
//    boolean isEmpty();
//
//    /**
//     * Fetches the stack currently stored at the given slot. If the slot is empty,
//     * or is outside the bounds of this inventory, returns see {@link ItemStack#EMPTY}.
//     */
//    ItemStack getStack(int var1);
//
//    /**
//     * Removes a specific number of items from the given slot.
//     *
//     * @return the removed items as a stack
//     */
//    ItemStack removeStack(int var1, int var2);
//
//    /**
//     * Removes the stack currently stored at the indicated slot.
//     *
//     * @return the stack previously stored at the indicated slot.
//     */
//    ItemStack removeStack(int var1);
//
//    void setStack(int var1, ItemStack var2);
//
//    /**
//     * Returns the maximum number of items a stack can contain when placed inside this inventory.
//     * No slots may have more than this number of items. It is effectively the
//     * stacking limit for this inventory's slots.
//     *
//     * @return the max {@link ItemStack#getCount() count} of item stacks in this inventory
//     */
//    default int getMaxCountPerStack() {
//        return 64;
//    }
//
//    void markDirty();
//
//    boolean canPlayerUse(PlayerEntity var1);
//
//    default void onOpen(PlayerEntity player) {
//    }
//
//    default void onClose(PlayerEntity player) {
//    }
//
//    /**
//     * Returns whether the given stack is a valid for the indicated slot position.
//     */
//    default boolean isValid(int slot, ItemStack stack) {
//        return true;
//    }
//
//    /**
//     * Returns the number of times the specified item occurs in this inventory across all stored stacks.
//     */
//    default int count(Item item) {
//        int i = 0;
//        for (int j = 0; j < this.size(); ++j) {
//            ItemStack itemStack = this.getStack(j);
//            if (!itemStack.getItem().equals(item)) continue;
//            i += itemStack.getCount();
//        }
//        return i;
//    }
//
//    /**
//     * Determines whether this inventory contains any of the given candidate items.
//     */
//    default boolean containsAny(Set<Item> items) {
//        for (int i = 0; i < this.size(); ++i) {
//            ItemStack itemStack = this.getStack(i);
//            if (!items.contains(itemStack.getItem()) || itemStack.getCount() <= 0) continue;
//            return true;
//        }
//        return false;
//    }
//}
//
