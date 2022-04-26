package com.github.cao.awa.hyacinth.server.entity.passive;

//public class PigEntity
//        extends AnimalEntity
//        implements ItemSteerable,
//        Saddleable {
//    private static final TrackedData<Boolean> SADDLED = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
//    private static final TrackedData<Integer> BOOST_TIME = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.INTEGER);
//    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.CARROT, Items.POTATO, Items.BEETROOT);
//    private final SaddledComponent saddledComponent;
//
//    public PigEntity(EntityType<? extends PigEntity> entityType, World world) {
//        super((EntityType<? extends AnimalEntity>)entityType, world);
//        this.saddledComponent = new SaddledComponent(this.dataTracker, BOOST_TIME, SADDLED);
//    }
//
//    @Override
//    protected void initGoals() {
//        this.goalSelector.add(0, new SwimGoal(this));
//        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
//        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
//        this.goalSelector.add(4, new TemptGoal(this, 1.2, Ingredient.ofItems(Items.CARROT_ON_A_STICK), false));
//        this.goalSelector.add(4, new TemptGoal(this, 1.2, BREEDING_INGREDIENT, false));
//        this.goalSelector.add(5, new FollowParentGoal(this, 1.1));
//        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
//        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
//        this.goalSelector.add(8, new LookAroundGoal(this));
//    }
//
//    public static DefaultAttributeContainer.Builder createPigAttributes() {
//        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
//    }
//
//    @Override
//    @Nullable
//    public Entity getPrimaryPassenger() {
//        return this.getFirstPassenger();
//    }
//
//    @Override
//    public boolean canBeControlledByRider() {
//        Entity entity = this.getPrimaryPassenger();
//        if (!(entity instanceof PlayerEntity)) {
//            return false;
//        }
//        PlayerEntity playerEntity = (PlayerEntity)entity;
//        return playerEntity.getMainHandStack().isOf(Items.CARROT_ON_A_STICK) || playerEntity.getOffHandStack().isOf(Items.CARROT_ON_A_STICK);
//    }
//
//    @Override
//    public void onTrackedDataSet(TrackedData<?> data) {
//        if (BOOST_TIME.equals(data) && this.world.isClient) {
//            this.saddledComponent.boost();
//        }
//        super.onTrackedDataSet(data);
//    }
//
//    @Override
//    protected void initDataTracker() {
//        super.initDataTracker();
//        this.dataTracker.startTracking(SADDLED, false);
//        this.dataTracker.startTracking(BOOST_TIME, 0);
//    }
//
//    @Override
//    public void writeCustomDataToNbt(NbtCompound nbt) {
//        super.writeCustomDataToNbt(nbt);
//        this.saddledComponent.writeNbt(nbt);
//    }
//
//    @Override
//    public void readCustomDataFromNbt(NbtCompound nbt) {
//        super.readCustomDataFromNbt(nbt);
//        this.saddledComponent.readNbt(nbt);
//    }
//
//    @Override
//    protected SoundEvent getAmbientSound() {
//        return SoundEvents.ENTITY_PIG_AMBIENT;
//    }
//
//    @Override
//    protected SoundEvent getHurtSound(DamageSource source) {
//        return SoundEvents.ENTITY_PIG_HURT;
//    }
//
//    @Override
//    protected SoundEvent getDeathSound() {
//        return SoundEvents.ENTITY_PIG_DEATH;
//    }
//
//    @Override
//    protected void playStepSound(BlockPos pos, BlockState state) {
//        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
//    }
//
//    @Override
//    public ActionResult interactMob(PlayerEntity player, Hand hand) {
//        boolean bl = this.isBreedingItem(player.getStackInHand(hand));
//        if (!bl && this.isSaddled() && !this.hasPassengers() && !player.shouldCancelInteraction()) {
//            if (!this.world.isClient) {
//                player.startRiding(this);
//            }
//            return ActionResult.success(this.world.isClient);
//        }
//        ActionResult actionResult = super.interactMob(player, hand);
//        if (!actionResult.isAccepted()) {
//            ItemStack itemStack = player.getStackInHand(hand);
//            if (itemStack.isOf(Items.SADDLE)) {
//                return itemStack.useOnEntity(player, this, hand);
//            }
//            return ActionResult.PASS;
//        }
//        return actionResult;
//    }
//
//    @Override
//    public boolean canBeSaddled() {
//        return this.isAlive() && !this.isBaby();
//    }
//
//    @Override
//    protected void dropInventory() {
//        super.dropInventory();
//        if (this.isSaddled()) {
//            this.dropItem(Items.SADDLE);
//        }
//    }
//
//    @Override
//    public boolean isSaddled() {
//        return this.saddledComponent.isSaddled();
//    }
//
//    @Override
//    public void saddle(@Nullable SoundCategory sound) {
//        this.saddledComponent.setSaddled(true);
//        if (sound != null) {
//            this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_PIG_SADDLE, sound, 0.5f, 1.0f);
//        }
//    }
//
//    @Override
//    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
//        Direction direction = this.getMovementDirection();
//        if (direction.getAxis() == Direction.Axis.Y) {
//            return super.updatePassengerForDismount(passenger);
//        }
//        int[][] is = Dismounting.getDismountOffsets(direction);
//        BlockPos blockPos = this.getBlockPos();
//        BlockPos.Mutable mutable = new BlockPos.Mutable();
//        for (EntityPose entityPose : passenger.getPoses()) {
//            Box box = passenger.getBoundingBox(entityPose);
//            for (int[] js : is) {
//                Vec3d vec3d;
//                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
//                double d = this.world.getDismountHeight(mutable);
//                if (!Dismounting.canDismountInBlock(d) || !Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d = Vec3d.ofCenter(mutable, d)))) continue;
//                passenger.setPose(entityPose);
//                return vec3d;
//            }
//        }
//        return super.updatePassengerForDismount(passenger);
//    }
//
//    @Override
//    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
//        if (world.getDifficulty() != Difficulty.PEACEFUL) {
//            ZombifiedPiglinEntity zombifiedPiglinEntity = EntityType.ZOMBIFIED_PIGLIN.create(world);
//            zombifiedPiglinEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
//            zombifiedPiglinEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
//            zombifiedPiglinEntity.setAiDisabled(this.isAiDisabled());
//            zombifiedPiglinEntity.setBaby(this.isBaby());
//            if (this.hasCustomName()) {
//                zombifiedPiglinEntity.setCustomName(this.getCustomName());
//                zombifiedPiglinEntity.setCustomNameVisible(this.isCustomNameVisible());
//            }
//            zombifiedPiglinEntity.setPersistent();
//            world.spawnEntity(zombifiedPiglinEntity);
//            this.discard();
//        } else {
//            super.onStruckByLightning(world, lightning);
//        }
//    }
//
//    @Override
//    public void travel(Vec3d movementInput) {
//        this.travel(this, this.saddledComponent, movementInput);
//    }
//
//    @Override
//    public float getSaddledSpeed() {
//        return (float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 0.225f;
//    }
//
//    @Override
//    public void setMovementInput(Vec3d movementInput) {
//        super.travel(movementInput);
//    }
//
//    @Override
//    public boolean consumeOnAStickItem() {
//        return this.saddledComponent.boost(this.getRandom());
//    }
//
//    @Override
//    public PigEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
//        return EntityType.PIG.create(serverWorld);
//    }
//
//    @Override
//    public boolean isBreedingItem(ItemStack stack) {
//        return BREEDING_INGREDIENT.test(stack);
//    }
//
//    @Override
//    public Vec3d getLeashOffset() {
//        return new Vec3d(0.0, 0.6f * this.getStandingEyeHeight(), this.getWidth() * 0.4f);
//    }
//}

import com.github.cao.awa.hyacinth.server.entity.*;
import com.github.cao.awa.hyacinth.server.world.*;

// TODO: 2022/4/26
public class PigEntity extends LivingEntity {

    public PigEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
