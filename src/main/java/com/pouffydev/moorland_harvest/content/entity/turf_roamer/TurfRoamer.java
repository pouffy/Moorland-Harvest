package com.pouffydev.moorland_harvest.content.entity.turf_roamer;

import com.pouffydev.moorland_harvest.registry.MoorlandBlocks;
import com.pouffydev.moorland_harvest.registry.MoorlandEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Iterator;

public class TurfRoamer extends HostileEntity {

	public TurfRoamer(EntityType<? extends HostileEntity> entityType,World world) {
		super(entityType, world);
		this.experiencePoints = 12;
	}
	protected void initGoals() {
		this.goalSelector.add(1, new MoveToDampTurfGoal(this));
		this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
		this.goalSelector.add(2, new AttackGoal(this));
		this.targetSelector.add(2, new TargetGoal<>(this, PlayerEntity.class, false));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, ChickenEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, CowEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, PigEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, SheepEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, RabbitEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, FoxEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, WolfEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, OcelotEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, ParrotEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, GoatEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, AxolotlEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, DonkeyEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, HorseEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, MuleEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, LlamaEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, TraderLlamaEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, PandaEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, PolarBearEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, StriderEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, CatEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, MooshroomEntity.class));
		this.targetSelector.add(3, new TargetAnimalGoal<>(this, FrogEntity.class));
		this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
		this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
	}
	private static class MoveToDampTurfGoal extends Goal {
		private final PathAwareEntity mob;
		public boolean canStart() {
			return this.mob.isOnGround() && !this.mob.world.getBlockState(this.mob.getBlockPos()).isOf(MoorlandBlocks.dampTurf);
		}
		MoveToDampTurfGoal(PathAwareEntity mob) {
			this.mob = mob;
		}
		public void start() {
			BlockPos blockPos = null;
			Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 2.0), MathHelper.floor(this.mob.getY() - 2.0), MathHelper.floor(this.mob.getZ() - 2.0), MathHelper.floor(this.mob.getX() + 2.0), this.mob.getBlockY(), MathHelper.floor(this.mob.getZ() + 2.0));

			for (BlockPos blockPos2 : iterable) {
				if (this.mob.world.getBlockState(blockPos2).isOf(MoorlandBlocks.dampTurf)) {
					blockPos = blockPos2;
					break;
				}
			}

			if (blockPos != null) {
				this.mob.getMoveControl().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2.0);
			}

		}
	}
	private static class AttackGoal extends MeleeAttackGoal {
		public AttackGoal(TurfRoamer stalker) {
			super(stalker, 1.0, true);
		}

		public boolean canStart() {
			return super.canStart() && !this.mob.hasPassengers();
		}

		public boolean shouldContinue() {
			float f = this.mob.getLightLevelDependentValue();
			if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
				this.mob.setTarget((LivingEntity)null);
				return false;
			} else {
				return super.shouldContinue();
			}
		}

		protected double getSquaredMaxAttackDistance(LivingEntity entity) {
			return 4.0F + entity.getWidth();
		}
	}
	private static class TargetAnimalGoal<T extends AnimalEntity> extends net.minecraft.entity.ai.goal.TargetGoal<T> {


		public TargetAnimalGoal(TurfRoamer stalker, Class<T> targetClass) {
			super(stalker, targetClass, true);
		}

		public boolean canStart() {
			float f = this.mob.getLightLevelDependentValue();
			return !(f >= 0.5F) && super.canStart();
		}
	}
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.13F;
	}

	public static DefaultAttributeContainer.Builder createTurfRoamerAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
	}

	protected Entity.MoveEffect getMoveEffect() {
		return MoveEffect.EVENTS;
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.BLOCK_SLIME_BLOCK_STEP;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.BLOCK_SLIME_BLOCK_HIT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SLIME_DEATH;
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 0.15F, 1.0F);
	}

	public void tick() {
		this.bodyYaw = this.getYaw();
		super.tick();
	}
	public void setBodyYaw(float bodyYaw) {
		this.setYaw(bodyYaw);
		super.setBodyYaw(bodyYaw);
	}
	public double getHeightOffset() {
		return 0.1;
	}
	protected EntityNavigation createNavigation(World world) {
		return new MobNavigation(this, world);
	}
	public void tickMovement() {
		super.tickMovement();
		RandomGenerator randomGenerator = world.getRandom();
		ParticleEffect turfRippleParticleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, MoorlandBlocks.dampTurf.getDefaultState());
		double d = MathHelper.nextDouble(randomGenerator, this.getX() - 0.20000000298023224, this.getX() + 0.20000000298023224);
		double e = MathHelper.nextDouble(randomGenerator, this.getY() + 0.5, this.getY() + 0.8999999761581421);
		double f = MathHelper.nextDouble(randomGenerator, this.getZ() - 0.20000000298023224, this.getZ() + 0.20000000298023224);
		//if (!world.getBlockState(this.getBlockPos().down()).isOf(MoorlandBlocks.dampTurf) || !world.getBlockState(this.getBlockPos()).isOf(MoorlandBlocks.dampTurf)) {
		//	this.discard();
		world.addParticle(turfRippleParticleEffect, d, e, f, 0.0, 0.0, 0.0);
		world.addParticle(turfRippleParticleEffect, d, e, f, 0.0, 0.0, 0.0);
		//}
	}
	public static boolean canSpawn(EntityType<TurfRoamer> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, RandomGenerator random) {
		if (canSpawnIgnoreLightLevel(type, world, spawnReason, pos, random) && world.getBlockState(pos.down()).isOf(MoorlandBlocks.dampTurf)) {
			PlayerEntity playerEntity = world.getClosestPlayer((double)pos.getX() + 3.5, (double)pos.getY() + 3.5, (double)pos.getZ() + 3.5, 12.0, true);
			return playerEntity == null;
		} else {
			return false;
		}
	}

	public static boolean canRoamerSpawn(EntityType<? extends MobEntity> mobEntityEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, RandomGenerator randomGenerator) {
		BlockPos pos = blockPos.down();
		return serverWorldAccess.getBlockState(blockPos).allowsSpawning(serverWorldAccess, blockPos, mobEntityEntityType) && serverWorldAccess.getBlockState(pos) == MoorlandBlocks.dampTurf.getDefaultState();
	}
}
