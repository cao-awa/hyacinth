//package com.github.cao.awa.hyacinth.network.handler.play;
//
//import java.util.Objects;
//
//import com.github.cao.awa.hyacinth.math.block.*;
//import com.github.cao.awa.hyacinth.network.packet.s2c.play.*;
//import com.github.cao.awa.hyacinth.server.block.*;
//import com.github.cao.awa.hyacinth.server.block.hit.*;
//import com.github.cao.awa.hyacinth.server.entity.player.*;
//import com.github.cao.awa.hyacinth.server.mode.*;
//import com.github.cao.awa.hyacinth.server.world.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.jetbrains.annotations.Nullable;
//
//public class ServerPlayerInteractionManager {
//    private static final Logger LOGGER = LogManager.getLogger();
//    protected ServerWorld world;
//    protected final ServerPlayerEntity player;
//    private GameMode gameMode = GameMode.DEFAULT;
//    @Nullable
//    private GameMode previousGameMode;
//    private boolean mining;
//    private int startMiningTime;
//    private BlockPos miningPos = BlockPos.ORIGIN;
//    private int tickCounter;
//    private boolean failedToMine;
//    private BlockPos failedMiningPos = BlockPos.ORIGIN;
//    private int failedStartMiningTime;
//    private int blockBreakingProgress = -1;
//
//    public ServerPlayerInteractionManager(ServerPlayerEntity player) {
//        this.player = player;
//        this.world = player.getWorld();
//    }
//
//    /**
//     * Checks if current game mode is different to {@code gameMode}, and change it if so.
//     *
//     * @return whether the current game mode has been changed
//     */
//    public boolean changeGameMode(GameMode gameMode) {
//        if (gameMode == this.gameMode) {
//            return false;
//        }
//        this.setGameMode(gameMode, this.gameMode);
//        return true;
//    }
//
//    protected void setGameMode(GameMode gameMode, @Nullable GameMode previousGameMode) {
//        this.previousGameMode = previousGameMode;
//        this.gameMode = gameMode;
//        gameMode.setAbilities(this.player.getAbilities());
//        this.player.sendAbilitiesUpdate();
//        this.player.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, this.player));
//        this.world.updateSleepingPlayers();
//    }
//
//    public GameMode getGameMode() {
//        return this.gameMode;
//    }
//
//    @Nullable
//    public GameMode getPreviousGameMode() {
//        return this.previousGameMode;
//    }
//
//    public boolean isSurvivalLike() {
//        return this.gameMode.isSurvivalLike();
//    }
//
//    public boolean isCreative() {
//        return this.gameMode.isCreative();
//    }
//
//    public void update() {
//        ++this.tickCounter;
//        if (this.failedToMine) {
//            BlockState blockState = this.world.getBlockState(this.failedMiningPos);
//            if (blockState.isAir()) {
//                this.failedToMine = false;
//            } else {
//                float f = this.continueMining(blockState, this.failedMiningPos, this.failedStartMiningTime);
//                if (f >= 1.0f) {
//                    this.failedToMine = false;
//                    this.tryBreakBlock(this.failedMiningPos);
//                }
//            }
//        } else if (this.mining) {
//            BlockState blockState = this.world.getBlockState(this.miningPos);
//            if (blockState.isAir()) {
//                this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
//                this.blockBreakingProgress = -1;
//                this.mining = false;
//            } else {
//                this.continueMining(blockState, this.miningPos, this.startMiningTime);
//            }
//        }
//    }
//
//    private float continueMining(BlockState state, BlockPos pos, int i) {
//        int j = this.tickCounter - i;
//        float f = state.calcBlockBreakingDelta(this.player, this.player.world, pos) * (float)(j + 1);
//        int k = (int)(f * 10.0f);
//        if (k != this.blockBreakingProgress) {
//            this.world.setBlockBreakingInfo(this.player.getId(), pos, k);
//            this.blockBreakingProgress = k;
//        }
//        return f;
//    }
//
//    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight) {
//        double f;
//        double e;
//        double d = this.player.getX() - ((double)pos.getX() + 0.5);
//        double g = d * d + (e = this.player.getY() - ((double)pos.getY() + 0.5) + 1.5) * e + (f = this.player.getZ() - ((double)pos.getZ() + 0.5)) * f;
//        if (g > 36.0) {
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too far"));
//            return;
//        }
//        if (pos.getY() >= worldHeight) {
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "too high"));
//            return;
//        }
//        if (action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
//            if (!this.world.canPlayerModifyAt(this.player, pos)) {
//                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "may not interact"));
//                return;
//            }
//            if (this.isCreative()) {
//                this.finishMining(pos, action, "creative destroy");
//                return;
//            }
//            if (this.player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) {
//                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, "block action restricted"));
//                return;
//            }
//            this.startMiningTime = this.tickCounter;
//            float h = 1.0f;
//            BlockState blockState = this.world.getBlockState(pos);
//            if (!blockState.isAir()) {
//                blockState.onBlockBreakStart(this.world, pos, this.player);
//                h = blockState.calcBlockBreakingDelta(this.player, this.player.world, pos);
//            }
//            if (!blockState.isAir() && h >= 1.0f) {
//                this.finishMining(pos, action, "insta mine");
//            } else {
//                if (this.mining) {
//                    this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
//                }
//                this.mining = true;
//                this.miningPos = pos.toImmutable();
//                int i = (int)(h * 10.0f);
//                this.world.setBlockBreakingInfo(this.player.getId(), pos, i);
//                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "actual start of destroying"));
//                this.blockBreakingProgress = i;
//            }
//        } else if (action == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
//            if (pos.equals(this.miningPos)) {
//                int h = this.tickCounter - this.startMiningTime;
//                BlockState blockState = this.world.getBlockState(pos);
//                if (!blockState.isAir()) {
//                    float i = blockState.calcBlockBreakingDelta(this.player, this.player.world, pos) * (float)(h + 1);
//                    if (i >= 0.7f) {
//                        this.mining = false;
//                        this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
//                        this.finishMining(pos, action, "destroyed");
//                        return;
//                    }
//                    if (!this.failedToMine) {
//                        this.mining = false;
//                        this.failedToMine = true;
//                        this.failedMiningPos = pos;
//                        this.failedStartMiningTime = this.startMiningTime;
//                    }
//                }
//            }
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "stopped destroying"));
//        } else if (action == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
//            this.mining = false;
//            if (!Objects.equals(this.miningPos, pos)) {
//                LOGGER.warn("Mismatch in destroy block pos: {} {}", (Object)this.miningPos, (Object)pos);
//                this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
//                this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos), action, true, "aborted mismatched destroying"));
//            }
//            this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, "aborted destroying"));
//        }
//    }
//
//    public void finishMining(BlockPos pos, PlayerActionC2SPacket.Action action, String reason) {
//        if (this.tryBreakBlock(pos)) {
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, true, reason));
//        } else {
//            this.player.networkHandler.sendPacket(new PlayerActionResponseS2CPacket(pos, this.world.getBlockState(pos), action, false, reason));
//        }
//    }
//
//    public boolean tryBreakBlock(BlockPos pos) {
//        BlockState blockState = this.world.getBlockState(pos);
//        if (!this.player.getMainHandStack().getItem().canMine(blockState, this.world, pos, this.player)) {
//            return false;
//        }
//        BlockEntity blockEntity = this.world.getBlockEntity(pos);
//        Block block = blockState.getBlock();
//        if (block instanceof OperatorBlock && !this.player.isCreativeLevelTwoOp()) {
//            this.world.updateListeners(pos, blockState, blockState, Block.NOTIFY_ALL);
//            return false;
//        }
//        if (this.player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) {
//            return false;
//        }
//        block.onBreak(this.world, pos, blockState, this.player);
//        boolean bl = this.world.removeBlock(pos, false);
//        if (bl) {
//            block.onBroken(this.world, pos, blockState);
//        }
//        if (this.isCreative()) {
//            return true;
//        }
//        ItemStack itemStack = this.player.getMainHandStack();
//        ItemStack itemStack2 = itemStack.copy();
//        boolean bl2 = this.player.canHarvest(blockState);
//        itemStack.postMine(this.world, blockState, pos, this.player);
//        if (bl && bl2) {
//            block.afterBreak(this.world, this.player, pos, blockState, blockEntity, itemStack2);
//        }
//        return true;
//    }
//
//    public ActionResult interactItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand) {
//        if (this.gameMode == GameMode.SPECTATOR) {
//            return ActionResult.PASS;
//        }
//        if (player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
//            return ActionResult.PASS;
//        }
//        int i = stack.getCount();
//        int j = stack.getDamage();
//        TypedActionResult<ItemStack> typedActionResult = stack.use(world, player, hand);
//        ItemStack itemStack = typedActionResult.getValue();
//        if (itemStack == stack && itemStack.getCount() == i && itemStack.getMaxUseTime() <= 0 && itemStack.getDamage() == j) {
//            return typedActionResult.getResult();
//        }
//        if (typedActionResult.getResult() == ActionResult.FAIL && itemStack.getMaxUseTime() > 0 && !player.isUsingItem()) {
//            return typedActionResult.getResult();
//        }
//        player.setStackInHand(hand, itemStack);
//        if (this.isCreative()) {
//            itemStack.setCount(i);
//            if (itemStack.isDamageable() && itemStack.getDamage() != j) {
//                itemStack.setDamage(j);
//            }
//        }
//        if (itemStack.isEmpty()) {
//            player.setStackInHand(hand, ItemStack.EMPTY);
//        }
//        if (!player.isUsingItem()) {
//            player.playerScreenHandler.syncState();
//        }
//        return typedActionResult.getResult();
//    }
//
//    public ActionResult interactBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult) {
//        ActionResult actionResult2;
//        Object actionResult;
//        BlockPos blockPos = hitResult.getBlockPos();
//        BlockState blockState = world.getBlockState(blockPos);
//        if (this.gameMode == GameMode.SPECTATOR) {
//            NamedScreenHandlerFactory namedScreenHandlerFactory = blockState.createScreenHandlerFactory(world, blockPos);
//            if (namedScreenHandlerFactory != null) {
//                player.openHandledScreen(namedScreenHandlerFactory);
//                return ActionResult.SUCCESS;
//            }
//            return ActionResult.PASS;
//        }
//        boolean namedScreenHandlerFactory = !player.getMainHandStack().isEmpty() || !player.getOffHandStack().isEmpty();
//        boolean bl = player.shouldCancelInteraction() && namedScreenHandlerFactory;
//        ItemStack itemStack = stack.copy();
//        if (!bl && (actionResult = blockState.onUse(world, player, hand, hitResult)).isAccepted()) {
//            Criteria.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
//            return actionResult;
//        }
//        if (stack.isEmpty() || player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
//            return ActionResult.PASS;
//        }
//        actionResult = new ItemUsageContext(player, hand, hitResult);
//        if (this.isCreative()) {
//            int i = stack.getCount();
//            actionResult2 = stack.useOnBlock((ItemUsageContext)actionResult);
//            stack.setCount(i);
//        } else {
//            actionResult2 = stack.useOnBlock((ItemUsageContext)actionResult);
//        }
//        if (actionResult2.isAccepted()) {
//            Criteria.ITEM_USED_ON_BLOCK.trigger(player, blockPos, itemStack);
//        }
//        return actionResult2;
//    }
//
//    public void setWorld(ServerWorld world) {
//        this.world = world;
//    }
//}
//
