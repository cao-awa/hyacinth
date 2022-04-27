//package com.github.cao.awa.hyacinth.server.item;
//
//import com.github.cao.awa.hyacinth.math.block.*;
//import com.github.cao.awa.hyacinth.network.text.*;
//import com.github.cao.awa.hyacinth.network.text.event.*;
//import com.github.cao.awa.hyacinth.network.text.style.*;
//import com.github.cao.awa.hyacinth.network.text.translate.*;
//import com.github.cao.awa.hyacinth.server.block.*;
//import com.github.cao.awa.hyacinth.server.entity.*;
//import com.github.cao.awa.hyacinth.server.entity.player.*;
//import com.github.cao.awa.hyacinth.server.world.*;
//import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
//import com.google.common.collect.*;
//import com.google.common.math.*;
//import com.google.gson.*;
//import com.mojang.brigadier.exceptions.*;
//import com.mojang.datafixers.kinds.*;
//import com.mojang.serialization.*;
//import com.mojang.serialization.codecs.*;
//import net.minecraft.nbt.*;
//import net.minecraft.tag.*;
//import net.minecraft.util.identifier.*;
//import net.minecraft.util.registry.*;
//import org.apache.logging.log4j.*;
//import org.jetbrains.annotations.*;
//
//import java.text.*;
//import java.util.*;
//import java.util.function.*;
//import java.util.stream.*;
//
///**
// * Represents a stack of items.
// *
// * <h2 id="nbt-operations">NBT operations</h2>
// *
// * <h3>NBT serialization</h3>
// *
// * An Item Stack can be serialized with {@link #writeNbt(NbtCompound)}, and deserialized with {@link #fromNbt(NbtCompound)}.
// *
// * <div class="fabric">
// * <table border=1>
// * <caption>Serialized NBT Structure</caption>
// * <tr>
// *   <th>Key</th><th>Type</th><th>Purpose</th>
// * </tr>
// * <tr>
// *   <td>{@code id}</td><td>{@link net.minecraft.nbt.NbtString}</td><td>The identifier of the item.</td>
// * </tr>
// * <tr>
// *   <td>{@code Count}</td><td>{@link net.minecraft.nbt.NbtByte}</td><td>The count of items in the stack.</td>
// * </tr>
// * <tr>
// *   <td>{@code tag}</td><td>{@link NbtCompound}</td><td>The item stack's custom NBT.</td>
// * </tr>
// * </table>
// * </div>
// *
// * <h3>Custom NBT</h3>
// *
// * The item stack's custom NBT may be used to store extra information,
// * like the block entity data for shulker boxes,
// * or the damage of a damageable item, etc.
// * <p>
// * Various methods are available to interact with the custom NBT, some methods might refer to a "sub NBT",
// * a sub NBT is a child element of the custom NBT.
// *
// * <div class="fabric">
// * <table border=1>
// * <caption>Custom NBT operations</caption>
// * <tr>
// *   <th>Category</th><th>Method</th><th>Summary</th>
// * </tr>
// * <tr>
// *   <td>Custom NBT</td><td>{@link #hasNbt()}</td><td>Returns whether the item stack has custom NBT.</td>
// * </tr>
// * <tr>
// *   <td>Custom NBT</td><td>{@link #getNbt()}</td><td>Returns the custom NBT of the item stack.</td>
// * </tr>
// * <tr>
// *   <td>Custom NBT</td><td>{@link #getOrCreateNbt()}</td><td>Returns the custom NBT of the item stack, or creates one if absent.</td>
// * </tr>
// * <tr>
// *   <td>Custom NBT</td><td>{@link #setNbt(NbtCompound)}</td><td>Sets the custom NBT of the item stack.</td>
// * </tr>
// * <tr>
// *   <td>Sub Custom NBT</td><td>{@link #getSubNbt(String)}</td><td>Returns the sub NBT compound at the specified key.</td>
// * </tr>
// * <tr>
// *   <td>Sub Custom NBT</td><td>{@link #getOrCreateSubNbt(String)}</td><td>Returns the sub NBT compound at the specified key, or create one if absent.</td>
// * </tr>
// * <tr>
// *   <td>Sub Custom NBT</td><td>{@link #removeSubNbt(String)}</td><td>Removes the sub NBT element at the specified key.</td>
// * </tr>
// * <tr>
// *   <td>Sub Custom NBT</td><td>{@link #setSubNbt(String, NbtElement)}</td><td>Sets the sub NBT element at the specified key.</td>
// * </tr>
// * </table>
// * </div>
// */
//public final class ItemStack {
//    public static final Codec<ItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec) Registry.ITEM.getCodec().fieldOf("id")).forGetter(stack -> stack.item), ((MapCodec)Codec.INT.fieldOf("Count")).forGetter(stack -> stack.count), NbtCompound.CODEC.optionalFieldOf("tag").forGetter(stack -> Optional.ofNullable(stack.nbt))).apply((Applicative<ItemStack, ?>)instance, ItemStack::new));
//    private static final Logger LOGGER = LogManager.getLogger();
//    public static final ItemStack EMPTY = new ItemStack((ItemConvertible)null);
//    public static final DecimalFormat MODIFIER_FORMAT = EntrustParser.operation(new DecimalFormat("#.##"), decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
//    /**
//     * The key of the enchantments in an item stack's custom NBT, whose value is {@value}.
//     */
//    public static final String ENCHANTMENTS_KEY = "Enchantments";
//    /**
//     * The key of the display NBT in an item stack's custom NBT, whose value is {@value}.
//     */
//    public static final String DISPLAY_KEY = "display";
//    /**
//     * The key of the item stack's name in the {@linkplain #DISPLAY_KEY display NBT}, whose value is {@value}.
//     */
//    public static final String NAME_KEY = "Name";
//    /**
//     * The key of the item stack's lore in the {@linkplain #DISPLAY_KEY display NBT}, whose value is {@value}.
//     */
//    public static final String LORE_KEY = "Lore";
//    /**
//     * The key of the damage in an item stack's custom NBT, whose value is {@value}.
//     */
//    public static final String DAMAGE_KEY = "Damage";
//    /**
//     * The key of the item's color in the {@linkplain #DISPLAY_KEY display NBT}, whose value is {@value}.
//     */
//    public static final String COLOR_KEY = "color";
//    /**
//     * The key of the unbreakable boolean in an item stack's custom NBT, whose value is {@value}.
//     */
//    private static final String UNBREAKABLE_KEY = "Unbreakable";
//    /**
//     * The key of the repair cost in an item stack's custom NBT, whose value is {@value}.
//     */
//    private static final String REPAIR_COST_KEY = "RepairCost";
//    private static final String CAN_DESTROY_KEY = "CanDestroy";
//    private static final String CAN_PLACE_ON_KEY = "CanPlaceOn";
//    private static final String HIDE_FLAGS_KEY = "HideFlags";
//    private static final int field_30903 = 0;
//    private static final Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(true);
//    private int count;
//    private int bobbingAnimationTime;
//    @Deprecated
//    private final Item item;
//    /**
//     * Repesents the item stack's custom NBT.
//     * <p>
//     * Stored at the key {@code tag} in the serialized item stack NBT.
//     *
//     * @see <a href="nbt-operations">Item Stack NBT Operations</a>
//     */
//    @Nullable
//    private NbtCompound nbt;
//    private boolean empty;
//    @Nullable
//    private Entity holder;
//    @Nullable
//    private BlockPredicatesChecker destroyChecker;
//    @Nullable
//    private BlockPredicatesChecker placeChecker;
//
//    public Optional<TooltipData> getTooltipData() {
//        return this.getItem().getTooltipData(this);
//    }
//
//    public ItemStack(ItemConvertible item) {
//        this(item, 1);
//    }
//
//    private ItemStack(ItemConvertible item, int count, Optional<NbtCompound> nbt) {
//        this(item, count);
//        nbt.ifPresent(this::setNbt);
//    }
//
//    public ItemStack(ItemConvertible item, int count) {
//        this.item = item == null ? null : item.asItem();
//        this.count = count;
//        if (this.item != null && this.item.isDamageable()) {
//            this.setDamage(this.getDamage());
//        }
//        this.updateEmptyState();
//    }
//
//    private void updateEmptyState() {
//        this.empty = false;
//        this.empty = this.isEmpty();
//    }
//
//    private ItemStack(NbtCompound nbt) {
//        this.item = Registry.ITEM.get(new Identifier(nbt.getString("id")));
//        this.count = nbt.getByte("Count");
//        if (nbt.contains("tag", 10)) {
//            this.nbt = nbt.getCompound("tag");
//            this.getItem().postProcessNbt(this.nbt);
//        }
//        if (this.getItem().isDamageable()) {
//            this.setDamage(this.getDamage());
//        }
//        this.updateEmptyState();
//    }
//
//    /**
//     * Deserializes an item stack from NBT.
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    public static ItemStack fromNbt(NbtCompound nbt) {
//        try {
//            return new ItemStack(nbt);
//        }
//        catch (RuntimeException runtimeException) {
//            LOGGER.debug("Tried to load invalid item: {}", (Object)nbt, (Object)runtimeException);
//            return EMPTY;
//        }
//    }
//
//    /**
//     * {@return whether this item stack is empty}
//     */
//    public boolean isEmpty() {
//        if (this == EMPTY) {
//            return true;
//        }
//        if (this.getItem() == null || this.isOf(Items.AIR)) {
//            return true;
//        }
//        return this.count <= 0;
//    }
//
//    public ItemStack split(int amount) {
//        int i = Math.min(amount, this.count);
//        ItemStack itemStack = this.copy();
//        itemStack.setCount(i);
//        this.decrement(i);
//        return itemStack;
//    }
//
//    public Item getItem() {
//        return this.empty ? Items.AIR : this.item;
//    }
//
//    public boolean isIn(Tag<Item> tag) {
//        return tag.contains(this.getItem());
//    }
//
//    public boolean isOf(Item item) {
//        return this.getItem() == item;
//    }
//
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        PlayerEntity playerEntity = context.getPlayer();
//        BlockPos blockPos = context.getBlockPos();
//        CachedBlockPosition cachedBlockPosition = new CachedBlockPosition(context.getWorld(), blockPos, false);
//        if (playerEntity != null && !playerEntity.getAbilities().allowModifyWorld && !this.canPlaceOn(context.getWorld().getTagManager(), cachedBlockPosition)) {
//            return ActionResult.PASS;
//        }
//        Item item = this.getItem();
//        ActionResult actionResult = item.useOnBlock(context);
//        if (playerEntity != null && actionResult.shouldIncrementStat()) {
//            playerEntity.incrementStat(Stats.USED.getOrCreateStat(item));
//        }
//        return actionResult;
//    }
//
//    public float getMiningSpeedMultiplier(BlockState state) {
//        return this.getItem().getMiningSpeedMultiplier(this, state);
//    }
//
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        return this.getItem().use(world, user, hand);
//    }
//
//    public ItemStack finishUsing(World world, LivingEntity user) {
//        return this.getItem().finishUsing(this, world, user);
//    }
//
//    /**
//     * Writes the serialized item stack into the given {@link NbtCompound}.
//     *
//     * @return the written NBT compound
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     *
//     * @param nbt the NBT compound to write to
//     */
//    public NbtCompound writeNbt(NbtCompound nbt) {
//        Identifier identifier = Registry.ITEM.getId(this.getItem());
//        nbt.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
//        nbt.putByte("Count", (byte)this.count);
//        if (this.nbt != null) {
//            nbt.put("tag", this.nbt.copy());
//        }
//        return nbt;
//    }
//
//    public int getMaxCount() {
//        return this.getItem().getMaxCount();
//    }
//
//    public boolean isStackable() {
//        return this.getMaxCount() > 1 && (!this.isDamageable() || !this.isDamaged());
//    }
//
//    public boolean isDamageable() {
//        if (this.empty || this.getItem().getMaxDamage() <= 0) {
//            return false;
//        }
//        NbtCompound nbtCompound = this.getNbt();
//        return nbtCompound == null || !nbtCompound.getBoolean(UNBREAKABLE_KEY);
//    }
//
//    public boolean isDamaged() {
//        return this.isDamageable() && this.getDamage() > 0;
//    }
//
//    public int getDamage() {
//        return this.nbt == null ? 0 : this.nbt.getInt(DAMAGE_KEY);
//    }
//
//    public void setDamage(int damage) {
//        this.getOrCreateNbt().putInt(DAMAGE_KEY, Math.max(0, damage));
//    }
//
//    public int getMaxDamage() {
//        return this.getItem().getMaxDamage();
//    }
//
//    public boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player) {
//        int i;
//        if (!this.isDamageable()) {
//            return false;
//        }
//        if (amount > 0) {
//            i = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, this);
//            int j = 0;
//            for (int k = 0; i > 0 && k < amount; ++k) {
//                if (!UnbreakingEnchantment.shouldPreventDamage(this, i, random)) continue;
//                ++j;
//            }
//            if ((amount -= j) <= 0) {
//                return false;
//            }
//        }
//        if (player != null && amount != 0) {
//            Criteria.ITEM_DURABILITY_CHANGED.trigger(player, this, this.getDamage() + amount);
//        }
//        i = this.getDamage() + amount;
//        this.setDamage(i);
//        return i >= this.getMaxDamage();
//    }
//
//    public <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback) {
//        if (entity instanceof PlayerEntity && ((PlayerEntity)entity).getAbilities().creativeMode) {
//            return;
//        }
//        if (!this.isDamageable()) {
//            return;
//        }
//        if (this.damage(amount, entity.getRandom(), entity instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity : null)) {
//            breakCallback.accept(entity);
//            Item item = this.getItem();
//            this.decrement(1);
//            if (entity instanceof PlayerEntity) {
//                ((PlayerEntity)entity).incrementStat(Stats.BROKEN.getOrCreateStat(item));
//            }
//            this.setDamage(0);
//        }
//    }
//
//    public boolean isItemBarVisible() {
//        return this.item.isItemBarVisible(this);
//    }
//
//    /**
//     * {@return the length of the filled section of the durability bar in pixels (out of 13)}
//     */
//    public int getItemBarStep() {
//        return this.item.getItemBarStep(this);
//    }
//
//    /**
//     * {@return the color of the filled section of the durability bar}
//     */
//    public int getItemBarColor() {
//        return this.item.getItemBarColor(this);
//    }
//
//    public boolean onStackClicked(Slot slot, ClickType clickType, PlayerEntity player) {
//        return this.getItem().onStackClicked(this, slot, clickType, player);
//    }
//
//    public boolean onClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
//        return this.getItem().onClicked(this, stack, slot, clickType, player, cursorStackReference);
//    }
//
//    public void postHit(LivingEntity target, PlayerEntity attacker) {
//        Item item = this.getItem();
//        if (item.postHit(this, target, attacker)) {
//            attacker.incrementStat(Stats.USED.getOrCreateStat(item));
//        }
//    }
//
//    public void postMine(World world, BlockState state, BlockPos pos, PlayerEntity miner) {
//        Item item = this.getItem();
//        if (item.postMine(this, world, state, pos, miner)) {
//            miner.incrementStat(Stats.USED.getOrCreateStat(item));
//        }
//    }
//
//    /**
//     * Determines whether this item can be used as a suitable tool for mining the specified block.
//     * <p>
//     * Depending on block implementation, when combined together, the correct item and block may achieve a better mining speed and yield
//     * drops that would not be obtained when mining otherwise.
//     *
//     * @return values consistent with calls to {@link Item#isSuitableFor}
//     * @see Item#isSuitableFor(BlockState)
//     */
//    public boolean isSuitableFor(BlockState state) {
//        return this.getItem().isSuitableFor(state);
//    }
//
//    public ActionResult useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand) {
//        return this.getItem().useOnEntity(this, user, entity, hand);
//    }
//
//    /**
//     * Creates and returns a copy of this item stack.
//     */
//    public ItemStack copy() {
//        if (this.isEmpty()) {
//            return EMPTY;
//        }
//        ItemStack itemStack = new ItemStack(this.getItem(), this.count);
//        itemStack.setBobbingAnimationTime(this.getBobbingAnimationTime());
//        if (this.nbt != null) {
//            itemStack.nbt = this.nbt.copy();
//        }
//        return itemStack;
//    }
//
//    /**
//     * {@return whether the given item stacks have equivalent custom NBT}
//     */
//    public static boolean areNbtEqual(ItemStack left, ItemStack right) {
//        if (left.isEmpty() && right.isEmpty()) {
//            return true;
//        }
//        if (left.isEmpty() || right.isEmpty()) {
//            return false;
//        }
//        if (left.nbt == null && right.nbt != null) {
//            return false;
//        }
//        return left.nbt == null || left.nbt.equals(right.nbt);
//    }
//
//    public static boolean areEqual(ItemStack left, ItemStack right) {
//        if (left.isEmpty() && right.isEmpty()) {
//            return true;
//        }
//        if (left.isEmpty() || right.isEmpty()) {
//            return false;
//        }
//        return left.isEqual(right);
//    }
//
//    private boolean isEqual(ItemStack stack) {
//        if (this.count != stack.count) {
//            return false;
//        }
//        if (!this.isOf(stack.getItem())) {
//            return false;
//        }
//        if (this.nbt == null && stack.nbt != null) {
//            return false;
//        }
//        return this.nbt == null || this.nbt.equals(stack.nbt);
//    }
//
//    public static boolean areItemsEqualIgnoreDamage(ItemStack left, ItemStack right) {
//        if (left == right) {
//            return true;
//        }
//        if (!left.isEmpty() && !right.isEmpty()) {
//            return left.isItemEqualIgnoreDamage(right);
//        }
//        return false;
//    }
//
//    public static boolean areItemsEqual(ItemStack left, ItemStack right) {
//        if (left == right) {
//            return true;
//        }
//        if (!left.isEmpty() && !right.isEmpty()) {
//            return left.isItemEqual(right);
//        }
//        return false;
//    }
//
//    public boolean isItemEqualIgnoreDamage(ItemStack stack) {
//        return !stack.isEmpty() && this.isOf(stack.getItem());
//    }
//
//    public boolean isItemEqual(ItemStack stack) {
//        if (this.isDamageable()) {
//            return !stack.isEmpty() && this.isOf(stack.getItem());
//        }
//        return this.isItemEqualIgnoreDamage(stack);
//    }
//
//    public static boolean canCombine(ItemStack stack, ItemStack otherStack) {
//        return stack.isOf(otherStack.getItem()) && ItemStack.areNbtEqual(stack, otherStack);
//    }
//
//    public String getTranslationKey() {
//        return this.getItem().getTranslationKey(this);
//    }
//
//    public String toString() {
//        return this.count + " " + this.getItem();
//    }
//
//    public void inventoryTick(World world, Entity entity, int slot, boolean selected) {
//        if (this.bobbingAnimationTime > 0) {
//            --this.bobbingAnimationTime;
//        }
//        if (this.getItem() != null) {
//            this.getItem().inventoryTick(this, world, entity, slot, selected);
//        }
//    }
//
//    public void onCraft(World world, PlayerEntity player, int amount) {
//        player.increaseStat(Stats.CRAFTED.getOrCreateStat(this.getItem()), amount);
//        this.getItem().onCraft(this, world, player);
//    }
//
//    public int getMaxUseTime() {
//        return this.getItem().getMaxUseTime(this);
//    }
//
//    public UseAction getUseAction() {
//        return this.getItem().getUseAction(this);
//    }
//
//    public void onStoppedUsing(World world, LivingEntity user, int remainingUseTicks) {
//        this.getItem().onStoppedUsing(this, world, user, remainingUseTicks);
//    }
//
//    public boolean isUsedOnRelease() {
//        return this.getItem().isUsedOnRelease(this);
//    }
//
//    /**
//     * {@return whether this item stack has custom NBT}
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    public boolean hasNbt() {
//        return !this.empty && this.nbt != null && !this.nbt.isEmpty();
//    }
//
//    /**
//     * {@return the custom NBT of this item stack, may be {@code null}}
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    @Nullable
//    public NbtCompound getNbt() {
//        return this.nbt;
//    }
//
//    /**
//     * Returns the custom NBT of this item stack, or creates the custom NBT if the item stack did not have a custom NBT previously.
//     *
//     * @return the custom NBT of this item stack
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    public NbtCompound getOrCreateNbt() {
//        if (this.nbt == null) {
//            this.setNbt(new NbtCompound());
//        }
//        return this.nbt;
//    }
//
//    /**
//     * {@return the compound NBT at the specified key in this item stack's NBT, or a new compound if absent}
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    public NbtCompound getOrCreateSubNbt(String key) {
//        if (this.nbt == null || !this.nbt.contains(key, 10)) {
//            NbtCompound nbtCompound = new NbtCompound();
//            this.setSubNbt(key, nbtCompound);
//            return nbtCompound;
//        }
//        return this.nbt.getCompound(key);
//    }
//
//    /**
//     * {@return the NBT compound at the specified key in this item stack's custom NBT, may be {@code null}}
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    @Nullable
//    public NbtCompound getSubNbt(String key) {
//        if (this.nbt == null || !this.nbt.contains(key, 10)) {
//            return null;
//        }
//        return this.nbt.getCompound(key);
//    }
//
//    /**
//     * Removes the sub NBT element at the specified key in this item stack's custom NBT.
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     */
//    public void removeSubNbt(String key) {
//        if (this.nbt != null && this.nbt.contains(key)) {
//            this.nbt.remove(key);
//            if (this.nbt.isEmpty()) {
//                this.nbt = null;
//            }
//        }
//    }
//
//    public NbtList getEnchantments() {
//        if (this.nbt != null) {
//            return this.nbt.getList(ENCHANTMENTS_KEY, 10);
//        }
//        return new NbtList();
//    }
//
//    /**
//     * Sets the custom NBT of this item stack.
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     *
//     * @param nbt the custom NBT compound, may be {@code null} to reset
//     */
//    public void setNbt(@Nullable NbtCompound nbt) {
//        this.nbt = nbt;
//        if (this.getItem().isDamageable()) {
//            this.setDamage(this.getDamage());
//        }
//        if (nbt != null) {
//            this.getItem().postProcessNbt(nbt);
//        }
//    }
//
//    public Text getName() {
//        NbtCompound nbtCompound = this.getSubNbt(DISPLAY_KEY);
//        if (nbtCompound != null && nbtCompound.contains(NAME_KEY, 8)) {
//            try {
//                MutableText text = Text.Serializer.fromJson(nbtCompound.getString(NAME_KEY));
//                if (text != null) {
//                    return text;
//                }
//                nbtCompound.remove(NAME_KEY);
//            }
//            catch (JsonParseException text) {
//                nbtCompound.remove(NAME_KEY);
//            }
//        }
//        return this.getItem().getName(this);
//    }
//
//    public ItemStack setCustomName(@Nullable Text name) {
//        NbtCompound nbtCompound = this.getOrCreateSubNbt(DISPLAY_KEY);
//        if (name != null) {
//            nbtCompound.putString(NAME_KEY, Text.Serializer.toJson(name));
//        } else {
//            nbtCompound.remove(NAME_KEY);
//        }
//        return this;
//    }
//
//    public void removeCustomName() {
//        NbtCompound nbtCompound = this.getSubNbt(DISPLAY_KEY);
//        if (nbtCompound != null) {
//            nbtCompound.remove(NAME_KEY);
//            if (nbtCompound.isEmpty()) {
//                this.removeSubNbt(DISPLAY_KEY);
//            }
//        }
//        if (this.nbt != null && this.nbt.isEmpty()) {
//            this.nbt = null;
//        }
//    }
//
//    public boolean hasCustomName() {
//        NbtCompound nbtCompound = this.getSubNbt(DISPLAY_KEY);
//        return nbtCompound != null && nbtCompound.contains(NAME_KEY, 8);
//    }
//
//    public List<Text> getTooltip(@Nullable PlayerEntity player, TooltipContext context) {
//        Multimap<EntityAttribute, EntityAttributeModifier> mutableText2;
//        Object string;
//        Object nbtCompound;
//        int integer;
//        Integer integer2;
//        ArrayList<Text> list = Lists.newArrayList();
//        MutableText mutableText = new LiteralText("").append(this.getName()).formatted(this.getRarity().formatting);
//        if (this.hasCustomName()) {
//            mutableText.formatted(Formatting.ITALIC);
//        }
//        list.add(mutableText);
//        if (!context.isAdvanced() && !this.hasCustomName() && this.isOf(Items.FILLED_MAP) && (integer2 = FilledMapItem.getMapId(this)) != null) {
//            list.add(new LiteralText("#" + integer2).formatted(Formatting.GRAY));
//        }
//        if (ItemStack.isSectionVisible(integer = this.getHideFlags(), TooltipSection.ADDITIONAL)) {
//            this.getItem().appendTooltip(this, player == null ? null : player.world, list, context);
//        }
//        if (this.hasNbt()) {
//            if (ItemStack.isSectionVisible(integer, TooltipSection.ENCHANTMENTS)) {
//                ItemStack.appendEnchantments(list, this.getEnchantments());
//            }
//            if (this.nbt.contains(DISPLAY_KEY, 10)) {
//                nbtCompound = this.nbt.getCompound(DISPLAY_KEY);
//                if (ItemStack.isSectionVisible(integer, TooltipSection.DYE) && ((NbtCompound)nbtCompound).contains(COLOR_KEY, 99)) {
//                    if (context.isAdvanced()) {
//                        list.add(new TranslatableText("item.color", String.format("#%06X", ((NbtCompound)nbtCompound).getInt(COLOR_KEY))).formatted(Formatting.GRAY));
//                    } else {
//                        list.add(new TranslatableText("item.dyed").formatted(Formatting.GRAY, Formatting.ITALIC));
//                    }
//                }
//                if (((NbtCompound)nbtCompound).getType(LORE_KEY) == 9) {
//                    NbtList nbtList = ((NbtCompound)nbtCompound).getList(LORE_KEY, 8);
//                    for (int i = 0; i < nbtList.size(); ++i) {
//                        string = nbtList.getString(i);
//                        try {
//                            mutableText2 = Text.Serializer.fromJson((String)string);
//                            if (mutableText2 == null) continue;
//                            list.add(Texts.setStyleIfAbsent((MutableText)((Object)mutableText2), LORE_STYLE));
//                            continue;
//                        }
//                        catch (JsonParseException mutableText22) {
//                            ((NbtCompound)nbtCompound).remove(LORE_KEY);
//                        }
//                    }
//                }
//            }
//        }
//        if (ItemStack.isSectionVisible(integer, TooltipSection.MODIFIERS)) {
//            nbtCompound = EquipmentSlot.values();
//            int nbtList = ((EquipmentSlot[])nbtCompound).length;
//            for (int i = 0; i < nbtList; ++i) {
//                string = nbtCompound[i];
//                mutableText2 = this.getAttributeModifiers((EquipmentSlot)((Object)string));
//                if (mutableText2.isEmpty()) continue;
//                list.add(LiteralText.EMPTY);
//                list.add(new TranslatableText("item.modifiers." + string.getName()).formatted(Formatting.GRAY));
//                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : mutableText2.entries()) {
//                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
//                    double d = entityAttributeModifier.getValue();
//                    boolean bl = false;
//                    if (player != null) {
//                        if (entityAttributeModifier.getId() == Item.ATTACK_DAMAGE_MODIFIER_ID) {
//                            d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
//                            d += (double)EnchantmentHelper.getAttackDamage(this, EntityGroup.DEFAULT);
//                            bl = true;
//                        } else if (entityAttributeModifier.getId() == Item.ATTACK_SPEED_MODIFIER_ID) {
//                            d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
//                            bl = true;
//                        }
//                    }
//                    double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? d * 100.0 : (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);
//                    if (bl) {
//                        list.add(new LiteralText(" ").append(new TranslatableText("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), MODIFIER_FORMAT.format(e), new TranslatableText(entry.getKey().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
//                        continue;
//                    }
//                    if (d > 0.0) {
//                        list.add(new TranslatableText("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), MODIFIER_FORMAT.format(e), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
//                        continue;
//                    }
//                    if (!(d < 0.0)) continue;
//                    list.add(new TranslatableText("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), MODIFIER_FORMAT.format(e *= -1.0), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
//                }
//            }
//        }
//        if (this.hasNbt()) {
//            if (ItemStack.isSectionVisible(integer, TooltipSection.UNBREAKABLE) && this.nbt.getBoolean(UNBREAKABLE_KEY)) {
//                list.add(new TranslatableText("item.unbreakable").formatted(Formatting.BLUE));
//            }
//            if (ItemStack.isSectionVisible(integer, TooltipSection.CAN_DESTROY) && this.nbt.contains(CAN_DESTROY_KEY, 9) && !((NbtList)(nbtCompound = this.nbt.getList(CAN_DESTROY_KEY, 8))).isEmpty()) {
//                list.add(LiteralText.EMPTY);
//                list.add(new TranslatableText("item.canBreak").formatted(Formatting.GRAY));
//                for (int nbtList = 0; nbtList < ((NbtList)nbtCompound).size(); ++nbtList) {
//                    list.addAll(ItemStack.parseBlockTag(((NbtList)nbtCompound).getString(nbtList)));
//                }
//            }
//            if (ItemStack.isSectionVisible(integer, TooltipSection.CAN_PLACE) && this.nbt.contains(CAN_PLACE_ON_KEY, 9) && !((NbtList)(nbtCompound = this.nbt.getList(CAN_PLACE_ON_KEY, 8))).isEmpty()) {
//                list.add(LiteralText.EMPTY);
//                list.add(new TranslatableText("item.canPlace").formatted(Formatting.GRAY));
//                for (int nbtList = 0; nbtList < ((NbtList)nbtCompound).size(); ++nbtList) {
//                    list.addAll(ItemStack.parseBlockTag(((NbtList)nbtCompound).getString(nbtList)));
//                }
//            }
//        }
//        if (context.isAdvanced()) {
//            if (this.isDamaged()) {
//                list.add(new TranslatableText("item.durability", this.getMaxDamage() - this.getDamage(), this.getMaxDamage()));
//            }
//            list.add(new LiteralText(Registry.ITEM.getId(this.getItem()).toString()).formatted(Formatting.DARK_GRAY));
//            if (this.hasNbt()) {
//                list.add(new TranslatableText("item.nbt_tags", this.nbt.getKeys().size()).formatted(Formatting.DARK_GRAY));
//            }
//        }
//        return list;
//    }
//
//    /**
//     * Determines whether the given tooltip section will be visible according to the given flags.
//     */
//    private static boolean isSectionVisible(int flags, TooltipSection tooltipSection) {
//        return (flags & tooltipSection.getFlag()) == 0;
//    }
//
//    private int getHideFlags() {
//        if (this.hasNbt() && this.nbt.contains(HIDE_FLAGS_KEY, 99)) {
//            return this.nbt.getInt(HIDE_FLAGS_KEY);
//        }
//        return 0;
//    }
//
//    public void addHideFlag(TooltipSection tooltipSection) {
//        NbtCompound nbtCompound = this.getOrCreateNbt();
//        nbtCompound.putInt(HIDE_FLAGS_KEY, nbtCompound.getInt(HIDE_FLAGS_KEY) | tooltipSection.getFlag());
//    }
//
//    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {
//        for (int i = 0; i < enchantments.size(); ++i) {
//            NbtCompound nbtCompound = enchantments.getCompound(i);
//            Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent(e -> tooltip.add(e.getName(EnchantmentHelper.getLevelFromNbt(nbtCompound))));
//        }
//    }
//
//    private static Collection<Text> parseBlockTag(String tag) {
//        try {
//            boolean bl2;
//            BlockArgumentParser blockArgumentParser = new BlockArgumentParser(new StringReader(tag), true).parse(true);
//            BlockState blockState = blockArgumentParser.getBlockState();
//            Identifier identifier = blockArgumentParser.getTagId();
//            boolean bl = blockState != null;
//            boolean bl3 = bl2 = identifier != null;
//            if (bl || bl2) {
//                List<Block> collection;
//                if (bl) {
//                    return Lists.newArrayList(blockState.getBlock().getName().formatted(Formatting.DARK_GRAY));
//                }
//                Tag<Block> tag2 = BlockTags.getTagGroup().getTag(identifier);
//                if (tag2 != null && !(collection = tag2.values()).isEmpty()) {
//                    return collection.stream().map(Block::getName).map(text -> text.formatted(Formatting.DARK_GRAY)).collect(Collectors.toList());
//                }
//            }
//        }
//        catch (CommandSyntaxException commandSyntaxException) {
//            // empty catch block
//        }
//        return Lists.newArrayList(new LiteralText("missingno").formatted(Formatting.DARK_GRAY));
//    }
//
//    public boolean hasGlint() {
//        return this.getItem().hasGlint(this);
//    }
//
//    public Rarity getRarity() {
//        return this.getItem().getRarity(this);
//    }
//
//    public boolean isEnchantable() {
//        if (!this.getItem().isEnchantable(this)) {
//            return false;
//        }
//        return !this.hasEnchantments();
//    }
//
//    public void addEnchantment(Enchantment enchantment, int level) {
//        this.getOrCreateNbt();
//        if (!this.nbt.contains(ENCHANTMENTS_KEY, 9)) {
//            this.nbt.put(ENCHANTMENTS_KEY, new NbtList());
//        }
//        NbtList nbtList = this.nbt.getList(ENCHANTMENTS_KEY, 10);
//        nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), (byte)level));
//    }
//
//    public boolean hasEnchantments() {
//        if (this.nbt != null && this.nbt.contains(ENCHANTMENTS_KEY, 9)) {
//            return !this.nbt.getList(ENCHANTMENTS_KEY, 10).isEmpty();
//        }
//        return false;
//    }
//
//    /**
//     * Sets the given NBT element in the item stack's custom NBT at the specified key.
//     *
//     * @see <a href="#nbt-operations">Item Stack NBT Operations</a>
//     *
//     * @param key the key where to put the given {@link NbtElement}
//     * @param element the NBT element to put
//     */
//    public void setSubNbt(String key, NbtElement element) {
//        this.getOrCreateNbt().put(key, element);
//    }
//
//    public boolean isInFrame() {
//        return this.holder instanceof ItemFrameEntity;
//    }
//
//    public void setHolder(@Nullable Entity holder) {
//        this.holder = holder;
//    }
//
//    @Nullable
//    public ItemFrameEntity getFrame() {
//        return this.holder instanceof ItemFrameEntity ? (ItemFrameEntity)this.getHolder() : null;
//    }
//
//    @Nullable
//    public Entity getHolder() {
//        return !this.empty ? this.holder : null;
//    }
//
//    public int getRepairCost() {
//        if (this.hasNbt() && this.nbt.contains(REPAIR_COST_KEY, 3)) {
//            return this.nbt.getInt(REPAIR_COST_KEY);
//        }
//        return 0;
//    }
//
//    public void setRepairCost(int repairCost) {
//        this.getOrCreateNbt().putInt(REPAIR_COST_KEY, repairCost);
//    }
//
//    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
//        Multimap<EntityAttribute, EntityAttributeModifier> multimap;
//        if (this.hasNbt() && this.nbt.contains("AttributeModifiers", 9)) {
//            multimap = HashMultimap.create();
//            NbtList nbtList = this.nbt.getList("AttributeModifiers", 10);
//            for (int i = 0; i < nbtList.size(); ++i) {
//                EntityAttributeModifier entityAttributeModifier;
//                Optional<EntityAttribute> optional;
//                NbtCompound nbtCompound = nbtList.getCompound(i);
//                if (nbtCompound.contains("Slot", 8) && !nbtCompound.getString("Slot").equals(slot.getName()) || !(optional = Registry.ATTRIBUTE.getOrEmpty(Identifier.tryParse(nbtCompound.getString("AttributeName")))).isPresent() || (entityAttributeModifier = EntityAttributeModifier.fromNbt(nbtCompound)) == null || entityAttributeModifier.getId().getLeastSignificantBits() == 0L || entityAttributeModifier.getId().getMostSignificantBits() == 0L) continue;
//                multimap.put(optional.get(), entityAttributeModifier);
//            }
//        } else {
//            multimap = this.getItem().getAttributeModifiers(slot);
//        }
//        return multimap;
//    }
//
//    public void addAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier, @Nullable EquipmentSlot slot) {
//        this.getOrCreateNbt();
//        if (!this.nbt.contains("AttributeModifiers", 9)) {
//            this.nbt.put("AttributeModifiers", new NbtList());
//        }
//        NbtList nbtList = this.nbt.getList("AttributeModifiers", 10);
//        NbtCompound nbtCompound = modifier.toNbt();
//        nbtCompound.putString("AttributeName", Registry.ATTRIBUTE.getId(attribute).toString());
//        if (slot != null) {
//            nbtCompound.putString("Slot", slot.getName());
//        }
//        nbtList.add(nbtCompound);
//    }
//
//    public Text toHoverableText() {
//        MutableText mutableText = new LiteralText("").append(this.getName());
//        if (this.hasCustomName()) {
//            mutableText.formatted(Formatting.ITALIC);
//        }
//        MutableText mutableText2 = Texts.bracketed(mutableText);
//        if (!this.empty) {
//            mutableText2.formatted(this.getRarity().formatting).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(this))));
//        }
//        return mutableText2;
//    }
//
//    public boolean canPlaceOn(TagManager tagManager, CachedBlockPosition pos) {
//        if (this.placeChecker == null) {
//            this.placeChecker = new BlockPredicatesChecker(CAN_PLACE_ON_KEY);
//        }
//        return this.placeChecker.check(this, tagManager, pos);
//    }
//
//    public boolean canDestroy(TagManager tagManager, CachedBlockPosition pos) {
//        if (this.destroyChecker == null) {
//            this.destroyChecker = new BlockPredicatesChecker(CAN_DESTROY_KEY);
//        }
//        return this.destroyChecker.check(this, tagManager, pos);
//    }
//
//    public int getBobbingAnimationTime() {
//        return this.bobbingAnimationTime;
//    }
//
//    public void setBobbingAnimationTime(int bobbingAnimationTime) {
//        this.bobbingAnimationTime = bobbingAnimationTime;
//    }
//
//    /**
//     * {@return the count of items in this item stack}
//     */
//    public int getCount() {
//        return this.empty ? 0 : this.count;
//    }
//
//    /**
//     * Sets the count of items in this item stack.
//     *
//     * @param count the count of items
//     */
//    public void setCount(int count) {
//        this.count = count;
//        this.updateEmptyState();
//    }
//
//    /**
//     * Increments the count of items in this item stack.
//     *
//     * @param amount the amount to increment
//     */
//    public void increment(int amount) {
//        this.setCount(this.count + amount);
//    }
//
//    /**
//     * Decrements the count of items in this item stack.
//     *
//     * @param amount the amount to decrement
//     */
//    public void decrement(int amount) {
//        this.increment(-amount);
//    }
//
//    public void usageTick(World world, LivingEntity user, int remainingUseTicks) {
//        this.getItem().usageTick(world, user, this, remainingUseTicks);
//    }
//
//    public void onItemEntityDestroyed(ItemEntity entity) {
//        this.getItem().onItemEntityDestroyed(entity);
//    }
//
//    public boolean isFood() {
//        return this.getItem().isFood();
//    }
//
//    public SoundEvent getDrinkSound() {
//        return this.getItem().getDrinkSound();
//    }
//
//    public SoundEvent getEatSound() {
//        return this.getItem().getEatSound();
//    }
//
//    @Nullable
//    public SoundEvent getEquipSound() {
//        return this.getItem().getEquipSound();
//    }
//
//    public static enum TooltipSection {
//        ENCHANTMENTS,
//        MODIFIERS,
//        UNBREAKABLE,
//        CAN_DESTROY,
//        CAN_PLACE,
//        ADDITIONAL,
//        DYE;
//
//        private final int flag = 1 << this.ordinal();
//
//        public int getFlag() {
//            return this.flag;
//        }
//    }
//}
//
