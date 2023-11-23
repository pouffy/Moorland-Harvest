package com.pouffydev.moorland_harvest.content.block;


import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.pouffydev.moorland_harvest.content.entity.turf_roamer.TurfRoamer;
import com.pouffydev.moorland_harvest.registry.MoorlandBlocks;
import com.pouffydev.moorland_harvest.registry.MoorlandEntityTags;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class DampTurfBlock extends Block {
	protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
	protected static final VoxelShape BOOTS_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
	protected static final VoxelShape ITEM_COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	public DampTurfBlock(Settings settings) {
		super(settings);
	}
	@Override
	public void onLandedUpon(@NotNull World world, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
		if (!((double)fallDistance < 4.0) && entity instanceof LivingEntity) {
			SoundEvent soundEvent = SoundEvents.BLOCK_SLIME_BLOCK_BREAK;
			entity.playSound(soundEvent, 1.0F, 1.0F);
		}
	}
	@Override
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		SoundEvent soundEvent = SoundEvents.BLOCK_SLIME_BLOCK_BREAK;
		projectile.playSound(soundEvent, 1.0F, 1.0F);
	}
	@Override
	public void onEntityCollision(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Entity entity) {
		if (!(entity instanceof TurfRoamer)) {
			entity.slowMovement(state, new Vec3d(0.7999999761581421, 0.15, 0.7999999761581421));
		}
		if (entity.getBlockStateAtPos().isOf(this)) {
			if (world.isClient) {
				RandomGenerator randomGenerator = world.getRandom();
				boolean bl = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
				if (bl && randomGenerator.nextBoolean()) {
					ParticleEffect turfRippleParticleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
					//spawn the particles around the top of the block at the feet of the entity
					double d = MathHelper.nextDouble(randomGenerator, entity.getX() - 0.20000000298023224, entity.getX() + 0.20000000298023224);
					double e = MathHelper.nextDouble(randomGenerator, entity.getY(), entity.getY() + 0.8999999761581421);
					double f = MathHelper.nextDouble(randomGenerator, entity.getZ() - 0.20000000298023224, entity.getZ() + 0.20000000298023224);
					world.addParticle(turfRippleParticleEffect, d, e, f, 0.0, 0.0, 0.0);
					world.addParticle(turfRippleParticleEffect, d, e, f, 0.0, 0.0, 0.0);
				}
			}
		}
		if (!world.isClient) {
			entity.setSprinting(false);
			if (entity.isOnFire() && (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || entity instanceof PlayerEntity) && entity.canModifyAt(world, pos)) {
				world.setBlockState(pos, MoorlandBlocks.driedTurf.getDefaultState());
				world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
				//push the entity up because dried turf has a full collision box
				entity.setVelocity(entity.getVelocity().add(0.0, 0.4, 0.0));
			}

			entity.setOnFire(false);
		}

	}
	public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
		if (context instanceof EntityShapeContext entityCollisionContext) {
			boolean hasNoBlockAbove = level.getBlockState(pos.up()).isAir();
			Entity entity = entityCollisionContext.getEntity();
			if (entity != null) {
				if (entity.fallDistance > 2.5F) {
					return COLLISION_SHAPE;
				}
				boolean bl = entity instanceof FallingBlockEntity;
				if (bl || canWalkOnDampTurf(entity) && context.isAbove(VoxelShapes.fullCube(), pos, false) && !context.isDescending() && hasNoBlockAbove) {
					return super.getCollisionShape(state, level, pos, context);
				}
				if (entity instanceof LivingEntity && ((LivingEntity)entity).getEquippedStack(EquipmentSlot.FEET).isOf(Items.LEATHER_BOOTS) || entity instanceof ArmorStandEntity && hasNoBlockAbove) {
					return BOOTS_COLLISION_SHAPE;
				}
				if (isEntitySinkable(context) && hasNoBlockAbove) {
					return ITEM_COLLISION_SHAPE;
				}
			}
		}
			return COLLISION_SHAPE;
	}

	public static boolean canWalkOnDampTurf(Entity entity) {
		return entity.getType().isIn(MoorlandEntityTags.DAMP_TURF_WALKABLE_MOBS);
	}

	public static boolean isEntitySinkable(ShapeContext context) {
		if (context instanceof EntityShapeContext entityCollisionContext) {
			Entity entity = entityCollisionContext.getEntity();
			return entity instanceof ProjectileEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof TurfRoamer;
		}
		return false;
	}
	@Override
	public ActionResult onUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() == Items.FLINT_AND_STEEL) {
			world.setBlockState(pos, MoorlandBlocks.driedTurf.getDefaultState());
			world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);

			itemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
			return ActionResult.success(world.isClient);
		}
		if (itemStack.getItem() == Items.FIRE_CHARGE) {
			world.setBlockState(pos, MoorlandBlocks.driedTurf.getDefaultState());
			world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);

			itemStack.decrement(1);
			return ActionResult.success(world.isClient);
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	public VoxelShape getCameraCollisionShape(@NotNull BlockState state, @NotNull BlockView level, @NotNull BlockPos pos, @NotNull ShapeContext context) {
		return VoxelShapes.fullCube();
	}
}
