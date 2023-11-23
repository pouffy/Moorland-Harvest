package com.pouffydev.moorland_harvest.content.entity.crow;

import com.pouffydev.moorland_harvest.content.entity.ai.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class CrowEntity extends AnimalEntity {
	private static final TrackedData<Boolean> FLYING = DataTracker.registerData(CrowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> ATTACK_TICK = DataTracker.registerData(CrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public float prevFlyProgress;
	public float flyProgress;
	public float prevAttackProgress;
	public float attackProgress;
	public int fleePumpkinFlag = 0;
	public boolean aiItemFlag = false;
	private boolean isLandNavigator;
	private int timeFlying = 0;
	private int heldItemTime = 0;
	private int checkPerchCooldown = 0;
	public float maxWingDeviation;
	public float prevMaxWingDeviation;
	public CrowEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
		switchNavigator(false);
	}
	public static DefaultAttributeContainer.Builder createCrowAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0);
	}
	private void flapWings() {
		this.prevFlyProgress = this.flyProgress;
		this.prevMaxWingDeviation = this.maxWingDeviation;
		this.maxWingDeviation += (float)(!this.onGround && !this.hasVehicle() ? 4 : -1) * 0.3F;
		this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
		if (!this.onGround && this.flyingSpeed < 1.0F) {
			this.flyingSpeed = 1.0F;
		}

		this.flyingSpeed *= 0.9F;
		Vec3d vec3d = this.getVelocity();
		if (!this.onGround && vec3d.y < 0.0) {
			this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
		}

		this.flyProgress += this.flyingSpeed * 2.0F;
	}

	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new CrowMeleeGoal(this));
		this.goalSelector.add(4, new CrowScatterGoal(this));
		this.goalSelector.add(5, new AvoidPumpkinsGoal(this));
		this.goalSelector.add(6, new CircleAndAttackCropsGoal(this));
		this.goalSelector.add(7, new AIWalkIdle());
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(9, new LookAtEntityGoal(this, PathAwareEntity.class, 6.0F));
		this.goalSelector.add(10, new LookAroundGoal(this));
		this.targetSelector.add(1, new CrowTargetItemsGoal(this, false, false, 40, 16));
	}
	public void peck() {
		this.dataTracker.set(ATTACK_TICK, 7);
	}
	private void switchNavigator(boolean onLand) {
		if (onLand) {
			this.moveControl = new MoveControl(this);
			this.navigation = new MobNavigation(this, world);
			this.isLandNavigator = true;
		} else {
			this.moveControl = new FlightMoveController(this, 0.7F, false);
			this.navigation = new DirectPathNavigator(this, world);
			this.isLandNavigator = false;
		}
	}
	public static <T extends MobEntity> boolean canCrowSpawn(EntityType<CrowEntity> crow, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, RandomGenerator randomGenerator) {
		final BlockState blockstate = serverWorldAccess.getBlockState(blockPos.down());
		return (blockstate.isIn(BlockTags.LEAVES) || blockstate.isOf(Blocks.GRASS_BLOCK) || blockstate.isIn(BlockTags.LOGS) || blockstate.isOf(Blocks.AIR)) && serverWorldAccess.getBaseLightLevel(blockPos, 0) > 8;
	}
	public boolean bypassesSteppingEffects() {
		return true;
	}
	public boolean handleFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	protected void fall(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.PUMPKIN_SEEDS;
	}
	public boolean isFlying() {
		return this.dataTracker.get(FLYING);
	}

	public void setFlying(boolean flying) {
		if(flying && isBaby()){
			return;
		}
		this.dataTracker.set(FLYING, flying);
	}
	public void tick() {
		super.tick();
		this.prevAttackProgress = attackProgress;
		prevFlyProgress = flyProgress;

		if (isFlying()) {
			if (flyProgress < 5F)
				flyProgress++;
		} else {
			if (flyProgress > 0F)
				flyProgress--;
		}

		if (fleePumpkinFlag > 0) {
			fleePumpkinFlag--;
		}

		if (!world.isClient) {
			final boolean isFlying = isFlying();
			if (isFlying && this.isLandNavigator) {
				switchNavigator(false);
			}
			if (!isFlying && !this.isLandNavigator) {
				switchNavigator(true);
			}
			if (isFlying) {
				timeFlying++;
				this.setFlying(true);
				if (this.isInLove()) {
					this.setFlying(false);
				}
			} else {
				timeFlying = 0;
				this.setNoGravity(false);
			}
		}
		if (!this.getMainHandStack().isEmpty()) {
			heldItemTime++;
			if (heldItemTime > 60 && isCrowEdible(this.getMainHandStack()) && (this.getHealth() < this.getMaxHealth())) {
				heldItemTime = 0;
				this.heal(4);
				this.emitGameEvent(GameEvent.EAT);
				this.playSound(SoundEvents.ENTITY_PARROT_EAT, this.getSoundVolume(), this.getSoundPitch());
				if (this.getMainHandStack().getItem() == Items.PUMPKIN_SEEDS) {
					if (getRandom().nextFloat() < 0.3F) {
						this.world.sendEntityStatus(this, (byte) 7);
					} else {
						this.world.sendEntityStatus(this, (byte) 6);
					}
				}
				if (this.getMainHandStack().getItem().hasRecipeRemainder()) {
					this.dropItem(this.getMainHandStack().getItem().getRecipeRemainder());
				}
				this.getMainHandStack().decrement(1);
			}
		} else {
			heldItemTime = 0;
		}
		if (this.dataTracker.get(ATTACK_TICK) > 0) {
			this.dataTracker.set(ATTACK_TICK, this.dataTracker.get(ATTACK_TICK) - 1);
			if (attackProgress < 5F) {
				attackProgress++;
			}
		} else {
			if (attackProgress > 0F) {
				attackProgress--;
			}
		}
		if(checkPerchCooldown > 0){
			checkPerchCooldown--;
		}
		this.flapWings();
	}
	private boolean isCrowEdible(ItemStack stack) {
		return stack.getItem().isFood();
	}
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}
	private boolean isOverWater() {
		BlockPos position = this.getBlockPos();
		while (position.getY() > -64 && world.isAir(position)) {
			position = position.down();
		}
		return !world.getFluidState(position).isEmpty();
	}
	public Vec3d getBlockGrounding(Vec3d fleePos) {
		final float radius = 3.15F * -3 - this.getRandom().nextInt(24);
		final float angle = getAngle1();
		final double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		final double extraZ = radius * MathHelper.cos(angle);
		final BlockPos radialPos = new BlockPos(fleePos.x + extraX, getY(), fleePos.z + extraZ);
		BlockPos ground = this.getCrowGround(radialPos);
		if (ground.getY() == -64) {
			return this.getPos();
		} else {
			ground = this.getBlockPos();
			while (ground.getY() > -64 && !world.getBlockState(ground).getMaterial().isSolid()) {
				ground = ground.down();
			}
		}
		if (!this.isTargetBlocked(Vec3d.ofCenter(ground.up()))) {
			return Vec3d.ofCenter(ground);
		}
		return null;
	}
	private float getAngle1() {
		final float neg = this.random.nextBoolean() ? 1 : -1;
		final float renderYawOffset = this.bodyYaw;
		return (0.0174532925F * renderYawOffset) + 3.15F + (this.random.nextFloat() * neg);
	}
	public boolean isTargetBlocked(Vec3d target) {
		Vec3d Vector3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());

		return this.world.raycast(new RaycastContext(Vector3d, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() != HitResult.Type.MISS;
	}
	public Vec3d getBlockInViewAway(Vec3d fleePos, float radiusAdd) {
		final float radius = 3.15F * -3 - this.random.nextInt(24) - radiusAdd;
		final float angle = getAngle1();
		final double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
		final double extraZ = radius * MathHelper.cos(angle);
		final BlockPos radialPos = new BlockPos(fleePos.x + extraX, 0, fleePos.z + extraZ);
		final BlockPos ground = getCrowGround(radialPos);
		final int distFromGround = (int) this.getY() - ground.getY();

		final BlockPos newPos;
		if (distFromGround > 8) {
			final int flightHeight = 4 + this.random.nextInt(10);
			newPos = ground.up(flightHeight);
		} else {
			newPos = ground.up(this.random.nextInt(6) + 1);
		}

		if (!this.isTargetBlocked(Vec3d.ofCenter(newPos)) && this.squaredDistanceTo(Vec3d.ofCenter(newPos)) > 1) {
			return Vec3d.ofCenter(newPos);
		}
		return null;
	}
	private BlockPos getCrowGround(BlockPos in){
		BlockPos position = new BlockPos(in.getX(), this.getY(), in.getZ());
		while (position.getY() > -64 && !world.getBlockState(position).getMaterial().isSolid() && world.getFluidState(position).isEmpty()) {
			position = position.down();
		}
		return position;
	}
	private class AIWalkIdle extends Goal {
		protected final CrowEntity crow;
		protected double x;
		protected double y;
		protected double z;
		private boolean flightTarget = false;

		public AIWalkIdle() {
			super();
			this.setControls(EnumSet.of(Control.MOVE));
			this.crow = CrowEntity.this;
		}
		@Override
		public boolean canStart() {
			if (CrowEntity.this.aiItemFlag || (crow.getTarget() != null && crow.getTarget().isAlive())) {
				return false;
			} else {
				if (this.crow.getRandom().nextInt(30) != 0 && !crow.isFlying()) {
					return false;
				}
				if (this.crow.isOnGround()) {
					this.flightTarget = random.nextBoolean();
				} else {
					this.flightTarget = random.nextInt(5) > 0 && crow.timeFlying < 200;
				}
				Vec3d position = this.getPosition();
				if (position == null) {
					return false;
				} else {
					this.x = position.x;
					this.y = position.y;
					this.z = position.z;
					return true;
				}
			}
		}
		@Nullable
		protected Vec3d getPosition() {
			final Vec3d vector3d = getPos();
			if(isOverWater()){
				flightTarget = true;
			}
			if (flightTarget) {
				if (timeFlying < 50 || isOverWater()) {
					return getBlockInViewAway(vector3d, 0);
				} else {
					return getBlockGrounding(vector3d);
				}
			} else {
				return FuzzyTargeting.find(this.crow, 10, 7);
			}
		}
		public void tick() {
			if (flightTarget) {
				crow.getMoveControl().moveTo(x, y, z, 1F);
			} else {
				this.crow.getNavigation().startMovingTo(this.x, this.y, this.z, 1F);

				if (isFlying() && crow.onGround) {
					crow.setFlying(false);
				}
			}

			if (isFlying() && crow.onGround && crow.timeFlying > 10) {
				crow.setFlying(false);
			}
		}

		public boolean shouldContinue() {
			if (crow.aiItemFlag) {
				return false;
			}
			if (flightTarget) {
				return crow.isFlying() && crow.squaredDistanceTo(x, y, z) > 2F;
			} else {
				return (!this.crow.getNavigation().isIdle());
			}
		}

		public void start() {
			if (flightTarget) {
				crow.setFlying(true);
				crow.getMoveControl().moveTo(x, y, z, 1F);
			} else {
				this.crow.getNavigation().startMovingTo(this.x, this.y, this.z, 1F);
			}
		}

		public void stop() {
			this.crow.getNavigation().stop();
			super.stop();
		}
	}
}
