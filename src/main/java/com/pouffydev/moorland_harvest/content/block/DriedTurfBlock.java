package com.pouffydev.moorland_harvest.content.block;

import com.pouffydev.moorland_harvest.registry.MoorlandBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class DriedTurfBlock extends Block {
	public DriedTurfBlock(Settings settings) {
		super(settings);
	}
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		if (world.hasRain(pos)) {
			world.setBlockState(pos, MoorlandBlocks.dampTurf.getDefaultState());
		}
	}
	@Override
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (projectile instanceof PotionEntity potionEntity) {
			ItemStack itemStack = potionEntity.getStack();
			Potion potion = PotionUtil.getPotion(itemStack);
			if (potion == Potions.WATER) {
				world.setBlockState(hit.getBlockPos(), MoorlandBlocks.dampTurf.getDefaultState());
			}
		}
	}
	@Override
	public ActionResult onUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (PotionUtil.getPotion(itemStack) == Potions.WATER ) {
			world.setBlockState(pos, MoorlandBlocks.dampTurf.getDefaultState());
			player.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}
}
