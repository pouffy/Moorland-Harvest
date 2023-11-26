package com.pouffydev.moorland_harvest.content.entity.ai;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import com.pouffydev.moorland_harvest.registry.MoorlandBlockTags;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AvoidPumpkinsGoal extends Goal {
	private CrowEntity crow;
	private final int searchLength;
	private final int verticalSearchRange;
	protected BlockPos destinationBlock;
	protected final EntityNavigation fleeingEntityNavigation;
	protected int runDelay = 70;
	private Vec3d flightTarget;

	public AvoidPumpkinsGoal(CrowEntity crow) {
		this.crow = crow;
		searchLength = 20;
		verticalSearchRange = 1;
		fleeingEntityNavigation = crow.getNavigation();
	}

	public boolean shouldContinue() {
		return destinationBlock != null && isPumpkin(crow.world, destinationBlock.mutableCopy()) && isCloseToPumpkin(16);
	}

	public boolean isCloseToPumpkin(double dist) {
		return destinationBlock == null || crow.squaredDistanceTo(Vec3d.ofCenter(destinationBlock)) < dist * dist;
	}
	@Override
	public boolean canStart() {
		if (this.runDelay > 0) {
			--this.runDelay;
			return false;
		} else {
			this.runDelay = 70 + crow.getRandom().nextInt(150);
			return this.searchForDestination();
		}
	}

	public void start() {
		crow.fleePumpkinFlag = 200;
		final Vec3d vec = crow.getBlockInViewAway(Vec3d.ofCenter(destinationBlock), 10);
		if (vec != null) {
			flightTarget = vec;
			crow.setFlying(true);
			fleeingEntityNavigation.startMovingTo(vec.x, vec.y, vec.z, 1F);
		}
	}

	public void tick() {
		if (this.isCloseToPumpkin(16)) {
			crow.fleePumpkinFlag = 200;
			if (flightTarget == null || crow.squaredDistanceTo(flightTarget) < 2F) {
				final Vec3d vec = crow.getBlockInViewAway(Vec3d.ofCenter(destinationBlock), 10);
				if (vec != null) {
					flightTarget = vec;
					crow.setFlying(true);
				}
			}
			if (flightTarget != null) {
				fleeingEntityNavigation.startMovingTo(flightTarget.x, flightTarget.y, flightTarget.z, 1F);
			}
		}
	}

	public void stop() {
		flightTarget = null;
	}

	protected boolean searchForDestination() {
		int length = this.searchLength;
		int searchRange = this.verticalSearchRange;
		BlockPos crowBlockPos = crow.getBlockPos();
		BlockPos.Mutable blockPos = new BlockPos.Mutable();

		for (int a = -8; a <= 2; a++) {
			for (int b = 0; b < length; ++b) {
				for (int c = 0; c <= b; c = c > 0 ? -c : 1 - c) {
					for (int d = c < b && c > -b ? b : 0; d <= b; d = d > 0 ? -d : 1 - d) {
						blockPos.set(crowBlockPos, c, a - 1, d);
						if (this.isPumpkin(crow.world, blockPos)) {
							this.destinationBlock = blockPos;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean isPumpkin(World world, BlockPos.Mutable blockPos) {
		return world.getBlockState(blockPos).isIn(MoorlandBlockTags.CROW_FEARS);
	}
}
