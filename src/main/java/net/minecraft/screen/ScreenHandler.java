//package net.minecraft.screen;
//
//import com.github.cao.awa.hyacinth.math.block.*;
//import com.github.cao.awa.hyacinth.server.block.*;
//import com.github.cao.awa.hyacinth.server.entity.player.*;
//import com.google.common.base.*;
//import com.google.common.base.Optional;
//import com.google.common.collect.*;
//import it.unimi.dsi.fastutil.ints.*;
//
//import java.util.*;
//
//public abstract class ScreenHandler {
//    /**
//     * A special slot index value ({@value}) indicating that the player has clicked outside the main panel
//     * of a screen. Used for dropping the cursor stack.
//     */
//    public static final int EMPTY_SPACE_SLOT_INDEX = -999;
//    public static final int field_30731 = 0;
//    public static final int field_30732 = 1;
//    public static final int field_30733 = 2;
//    public static final int field_30734 = 0;
//    public static final int field_30735 = 1;
//    public static final int field_30736 = 2;
//    public static final int field_30737 = Integer.MAX_VALUE;
//    /**
//     * A list of item stacks that is used for tracking changes in {@link #sendContentUpdates()}.
//     */
//    private final DefaultedList<ItemStack> trackedStacks = DefaultedList.of();
//    public final DefaultedList<Slot> slots = DefaultedList.of();
//    private final List<Property> properties = Lists.newArrayList();
//    private ItemStack cursorStack = ItemStack.EMPTY;
//    private final DefaultedList<ItemStack> previousTrackedStacks = DefaultedList.of();
//    private final IntList trackedPropertyValues = new IntArrayList();
//    private ItemStack previousCursorStack = ItemStack.EMPTY;
//    private int revision;
//    @Nullable
//    private final ScreenHandlerType<?> type;
//    public final int syncId;
//    private int quickCraftButton = -1;
//    private int quickCraftStage;
//    private final Set<Slot> quickCraftSlots = Sets.newHashSet();
//    private final List<ScreenHandlerListener> listeners = Lists.newArrayList();
//    @Nullable
//    private ScreenHandlerSyncHandler syncHandler;
//    private boolean disableSync;
//
//    protected ScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
//        this.type = type;
//        this.syncId = syncId;
//    }
//
//    protected static boolean canUse(ScreenHandlerContext context, PlayerEntity player, Block block) {
//        return context.get((world, pos) -> {
//            if (!world.getBlockState((BlockPos)pos).isOf(block)) {
//                return false;
//            }
//            return player.squaredDistanceTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5) <= 64.0;
//        }, true);
//    }
//
//    public ScreenHandlerType<?> getType() {
//        if (this.type == null) {
//            throw new UnsupportedOperationException("Unable to construct this menu by type");
//        }
//        return this.type;
//    }
//
//    /**
//     * Checks that the size of the provided inventory is at least as large as the {@code expectedSize}.
//     *
//     * @throws IllegalArgumentException if the inventory size is smaller than {@code expectedSize}
//     */
//    protected static void checkSize(Inventory inventory, int expectedSize) {
//        int i = inventory.size();
//        if (i < expectedSize) {
//            throw new IllegalArgumentException("Container size " + i + " is smaller than expected " + expectedSize);
//        }
//    }
//
//    /**
//     * Checks that the size of the {@code data} is at least as large as the {@code expectedCount}.
//     *
//     * @throws IllegalArgumentException if the {@code data} has a smaller size than {@code expectedCount}
//     */
//    protected static void checkDataCount(PropertyDelegate data, int expectedCount) {
//        int i = data.size();
//        if (i < expectedCount) {
//            throw new IllegalArgumentException("Container data count " + i + " is smaller than expected " + expectedCount);
//        }
//    }
//
//    protected Slot addSlot(Slot slot) {
//        slot.id = this.slots.size();
//        this.slots.add(slot);
//        this.trackedStacks.add(ItemStack.EMPTY);
//        this.previousTrackedStacks.add(ItemStack.EMPTY);
//        return slot;
//    }
//
//    protected Property addProperty(Property property) {
//        this.properties.add(property);
//        this.trackedPropertyValues.add(0);
//        return property;
//    }
//
//    protected void addProperties(PropertyDelegate propertyDelegate) {
//        for (int i = 0; i < propertyDelegate.size(); ++i) {
//            this.addProperty(Property.create(propertyDelegate, i));
//        }
//    }
//
//    public void addListener(ScreenHandlerListener listener) {
//        if (this.listeners.contains(listener)) {
//            return;
//        }
//        this.listeners.add(listener);
//        this.sendContentUpdates();
//    }
//
//    public void updateSyncHandler(ScreenHandlerSyncHandler handler) {
//        this.syncHandler = handler;
//        this.syncState();
//    }
//
//    public void syncState() {
//        int i;
//        int j = this.slots.size();
//        for (i = 0; i < j; ++i) {
//            this.previousTrackedStacks.set(i, this.slots.get(i).getStack().copy());
//        }
//        this.previousCursorStack = this.getCursorStack().copy();
//        j = this.properties.size();
//        for (i = 0; i < j; ++i) {
//            this.trackedPropertyValues.set(i, this.properties.get(i).get());
//        }
//        if (this.syncHandler != null) {
//            this.syncHandler.updateState(this, this.previousTrackedStacks, this.previousCursorStack, this.trackedPropertyValues.toIntArray());
//        }
//    }
//
//    public void removeListener(ScreenHandlerListener listener) {
//        this.listeners.remove(listener);
//    }
//
//    public DefaultedList<ItemStack> getStacks() {
//        DefaultedList<ItemStack> defaultedList = DefaultedList.of();
//        for (Slot slot : this.slots) {
//            defaultedList.add(slot.getStack());
//        }
//        return defaultedList;
//    }
//
//    /**
//     * Sends updates to listeners if any properties or slot stacks have changed.
//     */
//    public void sendContentUpdates() {
//        Object itemStack;
//        int i;
//        for (i = 0; i < this.slots.size(); ++i) {
//            itemStack = this.slots.get(i).getStack();
//            Supplier<ItemStack> supplier = Suppliers.memoize(((ItemStack)itemStack)::copy);
//            this.updateTrackedSlot(i, (ItemStack)itemStack, supplier);
//            this.checkSlotUpdates(i, (ItemStack)itemStack, supplier);
//        }
//        this.checkCursorStackUpdates();
//        for (i = 0; i < this.properties.size(); ++i) {
//            itemStack = this.properties.get(i);
//            int supplier = ((Property)itemStack).get();
//            if (((Property)itemStack).hasChanged()) {
//                this.notifyPropertyUpdate(i, supplier);
//            }
//            this.checkPropertyUpdates(i, supplier);
//        }
//    }
//
//    public void updateToClient() {
//        Object itemStack;
//        int i;
//        for (i = 0; i < this.slots.size(); ++i) {
//            itemStack = this.slots.get(i).getStack();
//            this.updateTrackedSlot(i, (ItemStack)itemStack, ((ItemStack)itemStack)::copy);
//        }
//        for (i = 0; i < this.properties.size(); ++i) {
//            itemStack = this.properties.get(i);
//            if (!((Property)itemStack).hasChanged()) continue;
//            this.notifyPropertyUpdate(i, ((Property)itemStack).get());
//        }
//        this.syncState();
//    }
//
//    private void notifyPropertyUpdate(int index, int value) {
//        for (ScreenHandlerListener screenHandlerListener : this.listeners) {
//            screenHandlerListener.onPropertyUpdate(this, index, value);
//        }
//    }
//
//    private void updateTrackedSlot(int slot, ItemStack stack, java.util.function.Supplier<ItemStack> copySupplier) {
//        ItemStack itemStack = this.trackedStacks.get(slot);
//        if (!ItemStack.areEqual(itemStack, stack)) {
//            ItemStack itemStack2 = copySupplier.get();
//            this.trackedStacks.set(slot, itemStack2);
//            for (ScreenHandlerListener screenHandlerListener : this.listeners) {
//                screenHandlerListener.onSlotUpdate(this, slot, itemStack2);
//            }
//        }
//    }
//
//    private void checkSlotUpdates(int slot, ItemStack stack, java.util.function.Supplier<ItemStack> copySupplier) {
//        if (this.disableSync) {
//            return;
//        }
//        ItemStack itemStack = this.previousTrackedStacks.get(slot);
//        if (!ItemStack.areEqual(itemStack, stack)) {
//            ItemStack itemStack2 = copySupplier.get();
//            this.previousTrackedStacks.set(slot, itemStack2);
//            if (this.syncHandler != null) {
//                this.syncHandler.updateSlot(this, slot, itemStack2);
//            }
//        }
//    }
//
//    private void checkPropertyUpdates(int id, int value) {
//        if (this.disableSync) {
//            return;
//        }
//        int i = this.trackedPropertyValues.getInt(id);
//        if (i != value) {
//            this.trackedPropertyValues.set(id, value);
//            if (this.syncHandler != null) {
//                this.syncHandler.updateProperty(this, id, value);
//            }
//        }
//    }
//
//    private void checkCursorStackUpdates() {
//        if (this.disableSync) {
//            return;
//        }
//        if (!ItemStack.areEqual(this.getCursorStack(), this.previousCursorStack)) {
//            this.previousCursorStack = this.getCursorStack().copy();
//            if (this.syncHandler != null) {
//                this.syncHandler.updateCursorStack(this, this.previousCursorStack);
//            }
//        }
//    }
//
//    public void setPreviousTrackedSlot(int slot, ItemStack stack) {
//        this.previousTrackedStacks.set(slot, stack.copy());
//    }
//
//    public void setPreviousTrackedSlotMutable(int slot, ItemStack stack) {
//        this.previousTrackedStacks.set(slot, stack);
//    }
//
//    public void setPreviousCursorStack(ItemStack stack) {
//        this.previousCursorStack = stack.copy();
//    }
//
//    public boolean onButtonClick(PlayerEntity player, int id) {
//        return false;
//    }
//
//    public Slot getSlot(int index) {
//        return this.slots.get(index);
//    }
//
//    public ItemStack transferSlot(PlayerEntity player, int index) {
//        return this.slots.get(index).getStack();
//    }
//
//    /**
//     * Performs a slot click. This can behave in many different ways depending mainly on the action type.
//     *
//     * @param actionType the type of slot click, check the docs for each {@link SlotActionType} value for details
//     */
//    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
//        try {
//            this.internalOnSlotClick(slotIndex, button, actionType, player);
//        }
//        catch (Exception exception) {
//            CrashReport crashReport = CrashReport.create(exception, "Container click");
//            CrashReportSection crashReportSection = crashReport.addElement("Click info");
//            crashReportSection.add("Menu Type", () -> this.type != null ? Registry.SCREEN_HANDLER.getId(this.type).toString() : "<no type>");
//            crashReportSection.add("Menu Class", () -> this.getClass().getCanonicalName());
//            crashReportSection.add("Slot Count", this.slots.size());
//            crashReportSection.add("Slot", slotIndex);
//            crashReportSection.add("Button", button);
//            crashReportSection.add("Type", (Object)actionType);
//            throw new CrashException(crashReport);
//        }
//    }
//
//    /**
//     * The actual logic that handles a slot click. Called by {@link #onSlotClick
//     * (int, int, SlotActionType, PlayerEntity)} in a try-catch block that wraps
//     * exceptions from this method into a crash report.
//     */
//    private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
//        block39: {
//            block50: {
//                block46: {
//                    ItemStack itemStack;
//                    ItemStack slot;
//                    Slot i;
//                    PlayerInventory playerInventory;
//                    block49: {
//                        block48: {
//                            block47: {
//                                block44: {
//                                    ClickType i2;
//                                    block45: {
//                                        block43: {
//                                            block37: {
//                                                block42: {
//                                                    ItemStack itemStack2;
//                                                    block41: {
//                                                        block40: {
//                                                            block38: {
//                                                                playerInventory = player.getInventory();
//                                                                if (actionType != SlotActionType.QUICK_CRAFT) break block37;
//                                                                int i3 = this.quickCraftStage;
//                                                                this.quickCraftStage = ScreenHandler.unpackQuickCraftStage(button);
//                                                                if (i3 == 1 && this.quickCraftStage == 2 || i3 == this.quickCraftStage) break block38;
//                                                                this.endQuickCraft();
//                                                                break block39;
//                                                            }
//                                                            if (!this.getCursorStack().isEmpty()) break block40;
//                                                            this.endQuickCraft();
//                                                            break block39;
//                                                        }
//                                                        if (this.quickCraftStage != 0) break block41;
//                                                        this.quickCraftButton = ScreenHandler.unpackQuickCraftButton(button);
//                                                        if (ScreenHandler.shouldQuickCraftContinue(this.quickCraftButton, player)) {
//                                                            this.quickCraftStage = 1;
//                                                            this.quickCraftSlots.clear();
//                                                        } else {
//                                                            this.endQuickCraft();
//                                                        }
//                                                        break block39;
//                                                    }
//                                                    if (this.quickCraftStage != 1) break block42;
//                                                    Slot slot2 = this.slots.get(slotIndex);
//                                                    if (!ScreenHandler.canInsertItemIntoSlot(slot2, itemStack2 = this.getCursorStack(), true) || !slot2.canInsert(itemStack2) || this.quickCraftButton != 2 && itemStack2.getCount() <= this.quickCraftSlots.size() || !this.canInsertIntoSlot(slot2)) break block39;
//                                                    this.quickCraftSlots.add(slot2);
//                                                    break block39;
//                                                }
//                                                if (this.quickCraftStage == 2) {
//                                                    if (!this.quickCraftSlots.isEmpty()) {
//                                                        if (this.quickCraftSlots.size() == 1) {
//                                                            int slot3 = this.quickCraftSlots.iterator().next().id;
//                                                            this.endQuickCraft();
//                                                            this.internalOnSlotClick(slot3, this.quickCraftButton, SlotActionType.PICKUP, player);
//                                                            return;
//                                                        }
//                                                        ItemStack slot4 = this.getCursorStack().copy();
//                                                        int itemStack3 = this.getCursorStack().getCount();
//                                                        for (Slot slot2 : this.quickCraftSlots) {
//                                                            ItemStack itemStack2 = this.getCursorStack();
//                                                            if (slot2 == null || !ScreenHandler.canInsertItemIntoSlot(slot2, itemStack2, true) || !slot2.canInsert(itemStack2) || this.quickCraftButton != 2 && itemStack2.getCount() < this.quickCraftSlots.size() || !this.canInsertIntoSlot(slot2)) continue;
//                                                            ItemStack itemStack32 = slot4.copy();
//                                                            int j = slot2.hasStack() ? slot2.getStack().getCount() : 0;
//                                                            ScreenHandler.calculateStackSize(this.quickCraftSlots, this.quickCraftButton, itemStack32, j);
//                                                            int k = Math.min(itemStack32.getMaxCount(), slot2.getMaxItemCount(itemStack32));
//                                                            if (itemStack32.getCount() > k) {
//                                                                itemStack32.setCount(k);
//                                                            }
//                                                            itemStack3 -= itemStack32.getCount() - j;
//                                                            slot2.setStack(itemStack32);
//                                                        }
//                                                        slot4.setCount(itemStack3);
//                                                        this.setCursorStack(slot4);
//                                                    }
//                                                    this.endQuickCraft();
//                                                } else {
//                                                    this.endQuickCraft();
//                                                }
//                                                break block39;
//                                            }
//                                            if (this.quickCraftStage == 0) break block43;
//                                            this.endQuickCraft();
//                                            break block39;
//                                        }
//                                        if (actionType != SlotActionType.PICKUP && actionType != SlotActionType.QUICK_MOVE || button != 0 && button != 1) break block44;
//                                        ClickType clickType = i2 = button == 0 ? ClickType.LEFT : ClickType.RIGHT;
//                                        if (slotIndex != EMPTY_SPACE_SLOT_INDEX) break block45;
//                                        if (this.getCursorStack().isEmpty()) break block39;
//                                        if (i2 == ClickType.LEFT) {
//                                            player.dropItem(this.getCursorStack(), true);
//                                            this.setCursorStack(ItemStack.EMPTY);
//                                        } else {
//                                            player.dropItem(this.getCursorStack().split(1), true);
//                                        }
//                                        break block39;
//                                    }
//                                    if (actionType == SlotActionType.QUICK_MOVE) {
//                                        if (slotIndex < 0) {
//                                            return;
//                                        }
//                                        Slot slot5 = this.slots.get(slotIndex);
//                                        if (!slot5.canTakeItems(player)) {
//                                            return;
//                                        }
//                                        ItemStack itemStack4 = this.transferSlot(player, slotIndex);
//                                        while (!itemStack4.isEmpty() && ItemStack.areItemsEqualIgnoreDamage(slot5.getStack(), itemStack4)) {
//                                            itemStack4 = this.transferSlot(player, slotIndex);
//                                        }
//                                    } else {
//                                        if (slotIndex < 0) {
//                                            return;
//                                        }
//                                        Slot slot6 = this.slots.get(slotIndex);
//                                        ItemStack itemStack5 = slot6.getStack();
//                                        ItemStack itemStack4 = this.getCursorStack();
//                                        player.onPickupSlotClick(itemStack4, slot6.getStack(), i2);
//                                        if (!itemStack4.onStackClicked(slot6, i2, player) && !itemStack5.onClicked(itemStack4, slot6, i2, player, this.getCursorStackReference())) {
//                                            if (itemStack5.isEmpty()) {
//                                                if (!itemStack4.isEmpty()) {
//                                                    int slot2 = i2 == ClickType.LEFT ? itemStack4.getCount() : 1;
//                                                    this.setCursorStack(slot6.insertStack(itemStack4, slot2));
//                                                }
//                                            } else if (slot6.canTakeItems(player)) {
//                                                if (itemStack4.isEmpty()) {
//                                                    int slot2 = i2 == ClickType.LEFT ? itemStack5.getCount() : (itemStack5.getCount() + 1) / 2;
//                                                    Optional<ItemStack> itemStack2 = slot6.tryTakeStackRange(slot2, Integer.MAX_VALUE, player);
//                                                    itemStack2.ifPresent(stack -> {
//                                                        this.setCursorStack((ItemStack)stack);
//                                                        slot6.onTakeItem(player, (ItemStack)stack);
//                                                    });
//                                                } else if (slot6.canInsert(itemStack4)) {
//                                                    if (ItemStack.canCombine(itemStack5, itemStack4)) {
//                                                        int slot2 = i2 == ClickType.LEFT ? itemStack4.getCount() : 1;
//                                                        this.setCursorStack(slot6.insertStack(itemStack4, slot2));
//                                                    } else if (itemStack4.getCount() <= slot6.getMaxItemCount(itemStack4)) {
//                                                        slot6.setStack(itemStack4);
//                                                        this.setCursorStack(itemStack5);
//                                                    }
//                                                } else if (ItemStack.canCombine(itemStack5, itemStack4)) {
//                                                    Optional<ItemStack> slot2 = slot6.tryTakeStackRange(itemStack5.getCount(), itemStack4.getMaxCount() - itemStack4.getCount(), player);
//                                                    slot2.ifPresent(stack -> {
//                                                        itemStack4.increment(stack.getCount());
//                                                        slot6.onTakeItem(player, (ItemStack)stack);
//                                                    });
//                                                }
//                                            }
//                                        }
//                                        slot6.markDirty();
//                                    }
//                                    break block39;
//                                }
//                                if (actionType != SlotActionType.SWAP) break block46;
//                                i = this.slots.get(slotIndex);
//                                slot = playerInventory.getStack(button);
//                                itemStack = i.getStack();
//                                if (slot.isEmpty() && itemStack.isEmpty()) break block39;
//                                if (!slot.isEmpty()) break block47;
//                                if (!i.canTakeItems(player)) break block39;
//                                playerInventory.setStack(button, itemStack);
//                                i.onTake(itemStack.getCount());
//                                i.setStack(ItemStack.EMPTY);
//                                i.onTakeItem(player, itemStack);
//                                break block39;
//                            }
//                            if (!itemStack.isEmpty()) break block48;
//                            if (!i.canInsert(slot)) break block39;
//                            int itemStack4 = i.getMaxItemCount(slot);
//                            if (slot.getCount() > itemStack4) {
//                                i.setStack(slot.split(itemStack4));
//                            } else {
//                                playerInventory.setStack(button, ItemStack.EMPTY);
//                                i.setStack(slot);
//                            }
//                            break block39;
//                        }
//                        if (!i.canTakeItems(player) || !i.canInsert(slot)) break block39;
//                        int itemStack4 = i.getMaxItemCount(slot);
//                        if (slot.getCount() <= itemStack4) break block49;
//                        i.setStack(slot.split(itemStack4));
//                        i.onTakeItem(player, itemStack);
//                        if (playerInventory.insertStack(itemStack)) break block39;
//                        player.dropItem(itemStack, true);
//                        break block39;
//                    }
//                    playerInventory.setStack(button, itemStack);
//                    i.setStack(slot);
//                    i.onTakeItem(player, itemStack);
//                    break block39;
//                }
//                if (actionType != SlotActionType.CLONE || !player.getAbilities().creativeMode || !this.getCursorStack().isEmpty() || slotIndex < 0) break block50;
//                Slot i = this.slots.get(slotIndex);
//                if (!i.hasStack()) break block39;
//                ItemStack slot = i.getStack().copy();
//                slot.setCount(slot.getMaxCount());
//                this.setCursorStack(slot);
//                break block39;
//            }
//            if (actionType == SlotActionType.THROW && this.getCursorStack().isEmpty() && slotIndex >= 0) {
//                Slot i = this.slots.get(slotIndex);
//                int slot = button == 0 ? 1 : i.getStack().getCount();
//                ItemStack itemStack = i.takeStackRange(slot, Integer.MAX_VALUE, player);
//                player.dropItem(itemStack, true);
//            } else if (actionType == SlotActionType.PICKUP_ALL && slotIndex >= 0) {
//                Slot i = this.slots.get(slotIndex);
//                ItemStack slot = this.getCursorStack();
//                if (!(slot.isEmpty() || i.hasStack() && i.canTakeItems(player))) {
//                    int itemStack = button == 0 ? 0 : this.slots.size() - 1;
//                    int itemStack4 = button == 0 ? 1 : -1;
//                    for (int slot2 = 0; slot2 < 2; ++slot2) {
//                        for (int itemStack2 = itemStack; itemStack2 >= 0 && itemStack2 < this.slots.size() && slot.getCount() < slot.getMaxCount(); itemStack2 += itemStack4) {
//                            Slot itemStack3 = this.slots.get(itemStack2);
//                            if (!itemStack3.hasStack() || !ScreenHandler.canInsertItemIntoSlot(itemStack3, slot, true) || !itemStack3.canTakeItems(player) || !this.canInsertIntoSlot(slot, itemStack3)) continue;
//                            ItemStack j = itemStack3.getStack();
//                            if (slot2 == 0 && j.getCount() == j.getMaxCount()) continue;
//                            ItemStack k = itemStack3.takeStackRange(j.getCount(), slot.getMaxCount() - slot.getCount(), player);
//                            slot.increment(k.getCount());
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private StackReference getCursorStackReference() {
//        return new StackReference(){
//
//            @Override
//            public ItemStack get() {
//                return ScreenHandler.this.getCursorStack();
//            }
//
//            @Override
//            public boolean set(ItemStack stack) {
//                ScreenHandler.this.setCursorStack(stack);
//                return true;
//            }
//        };
//    }
//
//    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
//        return true;
//    }
//
//    public void close(PlayerEntity player) {
//        ItemStack itemStack;
//        if (player instanceof ServerPlayerEntity && !(itemStack = this.getCursorStack()).isEmpty()) {
//            if (!player.isAlive() || ((ServerPlayerEntity)player).isDisconnected()) {
//                player.dropItem(itemStack, false);
//            } else {
//                player.getInventory().offerOrDrop(itemStack);
//            }
//            this.setCursorStack(ItemStack.EMPTY);
//        }
//    }
//
//    protected void dropInventory(PlayerEntity player, Inventory inventory) {
//        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity)player).isDisconnected()) {
//            for (int i = 0; i < inventory.size(); ++i) {
//                player.dropItem(inventory.removeStack(i), false);
//            }
//            return;
//        }
//        for (int i = 0; i < inventory.size(); ++i) {
//            PlayerInventory playerInventory = player.getInventory();
//            if (!(playerInventory.player instanceof ServerPlayerEntity)) continue;
//            playerInventory.offerOrDrop(inventory.removeStack(i));
//        }
//    }
//
//    public void onContentChanged(Inventory inventory) {
//        this.sendContentUpdates();
//    }
//
//    public void setStackInSlot(int slot, int revision, ItemStack stack) {
//        this.getSlot(slot).setStack(stack);
//        this.revision = revision;
//    }
//
//    public void updateSlotStacks(int revision, List<ItemStack> stacks, ItemStack cursorStack) {
//        for (int i = 0; i < stacks.size(); ++i) {
//            this.getSlot(i).setStack(stacks.get(i));
//        }
//        this.cursorStack = cursorStack;
//        this.revision = revision;
//    }
//
//    public void setProperty(int id, int value) {
//        this.properties.get(id).set(value);
//    }
//
//    public abstract boolean canUse(PlayerEntity var1);
//
//    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
//        ItemStack itemStack;
//        Slot slot;
//        boolean bl = false;
//        int i = startIndex;
//        if (fromLast) {
//            i = endIndex - 1;
//        }
//        if (stack.isStackable()) {
//            while (!stack.isEmpty() && (fromLast ? i >= startIndex : i < endIndex)) {
//                slot = this.slots.get(i);
//                itemStack = slot.getStack();
//                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
//                    int j = itemStack.getCount() + stack.getCount();
//                    if (j <= stack.getMaxCount()) {
//                        stack.setCount(0);
//                        itemStack.setCount(j);
//                        slot.markDirty();
//                        bl = true;
//                    } else if (itemStack.getCount() < stack.getMaxCount()) {
//                        stack.decrement(stack.getMaxCount() - itemStack.getCount());
//                        itemStack.setCount(stack.getMaxCount());
//                        slot.markDirty();
//                        bl = true;
//                    }
//                }
//                if (fromLast) {
//                    --i;
//                    continue;
//                }
//                ++i;
//            }
//        }
//        if (!stack.isEmpty()) {
//            i = fromLast ? endIndex - 1 : startIndex;
//            while (fromLast ? i >= startIndex : i < endIndex) {
//                slot = this.slots.get(i);
//                itemStack = slot.getStack();
//                if (itemStack.isEmpty() && slot.canInsert(stack)) {
//                    if (stack.getCount() > slot.getMaxItemCount()) {
//                        slot.setStack(stack.split(slot.getMaxItemCount()));
//                    } else {
//                        slot.setStack(stack.split(stack.getCount()));
//                    }
//                    slot.markDirty();
//                    bl = true;
//                    break;
//                }
//                if (fromLast) {
//                    --i;
//                    continue;
//                }
//                ++i;
//            }
//        }
//        return bl;
//    }
//
//    public static int unpackQuickCraftButton(int quickCraftData) {
//        return quickCraftData >> 2 & 3;
//    }
//
//    public static int unpackQuickCraftStage(int quickCraftData) {
//        return quickCraftData & 3;
//    }
//
//    public static int packQuickCraftData(int quickCraftStage, int buttonId) {
//        return quickCraftStage & 3 | (buttonId & 3) << 2;
//    }
//
//    public static boolean shouldQuickCraftContinue(int stage, PlayerEntity player) {
//        if (stage == 0) {
//            return true;
//        }
//        if (stage == 1) {
//            return true;
//        }
//        return stage == 2 && player.getAbilities().creativeMode;
//    }
//
//    protected void endQuickCraft() {
//        this.quickCraftStage = 0;
//        this.quickCraftSlots.clear();
//    }
//
//    public static boolean canInsertItemIntoSlot(@Nullable Slot slot, ItemStack stack, boolean allowOverflow) {
//        boolean bl;
//        boolean bl2 = bl = slot == null || !slot.hasStack();
//        if (!bl && ItemStack.canCombine(stack, slot.getStack())) {
//            return slot.getStack().getCount() + (allowOverflow ? 0 : stack.getCount()) <= stack.getMaxCount();
//        }
//        return bl;
//    }
//
//    public static void calculateStackSize(Set<Slot> slots, int mode, ItemStack stack, int stackSize) {
//        switch (mode) {
//            case 0: {
//                stack.setCount(MathHelper.floor((float)stack.getCount() / (float)slots.size()));
//                break;
//            }
//            case 1: {
//                stack.setCount(1);
//                break;
//            }
//            case 2: {
//                stack.setCount(stack.getItem().getMaxCount());
//            }
//        }
//        stack.increment(stackSize);
//    }
//
//    public boolean canInsertIntoSlot(Slot slot) {
//        return true;
//    }
//
//    public static int calculateComparatorOutput(@Nullable BlockEntity entity) {
//        if (entity instanceof Inventory) {
//            return ScreenHandler.calculateComparatorOutput((Inventory)((Object)entity));
//        }
//        return 0;
//    }
//
//    public static int calculateComparatorOutput(@Nullable Inventory inventory) {
//        if (inventory == null) {
//            return 0;
//        }
//        int i = 0;
//        float f = 0.0f;
//        for (int j = 0; j < inventory.size(); ++j) {
//            ItemStack itemStack = inventory.getStack(j);
//            if (itemStack.isEmpty()) continue;
//            f += (float)itemStack.getCount() / (float)Math.min(inventory.getMaxCountPerStack(), itemStack.getMaxCount());
//            ++i;
//        }
//        return MathHelper.floor((f /= (float)inventory.size()) * 14.0f) + (i > 0 ? 1 : 0);
//    }
//
//    public void setCursorStack(ItemStack stack) {
//        this.cursorStack = stack;
//    }
//
//    public ItemStack getCursorStack() {
//        return this.cursorStack;
//    }
//
//    public void disableSyncing() {
//        this.disableSync = true;
//    }
//
//    public void enableSyncing() {
//        this.disableSync = false;
//    }
//
//    public void copySharedSlots(ScreenHandler handler) {
//        Slot slot;
//        int i;
//        HashBasedTable<Inventory, Integer, Integer> table = HashBasedTable.create();
//        for (i = 0; i < handler.slots.size(); ++i) {
//            slot = handler.slots.get(i);
//            table.put(slot.inventory, slot.getIndex(), i);
//        }
//        for (i = 0; i < this.slots.size(); ++i) {
//            slot = this.slots.get(i);
//            Integer integer = (Integer)table.get(slot.inventory, slot.getIndex());
//            if (integer == null) continue;
//            this.trackedStacks.set(i, handler.trackedStacks.get(integer));
//            this.previousTrackedStacks.set(i, handler.previousTrackedStacks.get(integer));
//        }
//    }
//
//    public OptionalInt getSlotIndex(Inventory inventory, int index) {
//        for (int i = 0; i < this.slots.size(); ++i) {
//            Slot slot = this.slots.get(i);
//            if (slot.inventory != inventory || index != slot.getIndex()) continue;
//            return OptionalInt.of(i);
//        }
//        return OptionalInt.empty();
//    }
//
//    public int getRevision() {
//        return this.revision;
//    }
//
//    public int nextRevision() {
//        this.revision = this.revision + 1 & Short.MAX_VALUE;
//        return this.revision;
//    }
//}
//
