package com.pouffydev.moorland_harvest.content.entity.ai;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import com.pouffydev.moorland_harvest.registry.MoorlandBlockTags;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AvoidPumpkinsGoal extends Goal {
	private CrowEntity crow;
	private final int searchLength;
	private final int verticalSearchRange;
	protected BlockPos destinationBlock;
	protected int runDelay = 70;
	private Vec3d flightTarget;

	public AvoidPumpkinsGoal(CrowEntity crow) {
		this.crow = crow;
		searchLength = 20;
		verticalSearchRange = 1;
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
			crow.getMoveControl().moveTo(vec.x, vec.y, vec.z, 1F);
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
				crow.getMoveControl().moveTo(flightTarget.x, flightTarget.y, flightTarget.z, 1F);
			}
		}
	}

	public void stop() {
		flightTarget = null;
	}

	protected boolean searchForDestination() {
		int lvt_1_1_ = this.searchLength;
		int lvt_2_1_ = this.verticalSearchRange;
		BlockPos lvt_3_1_ = crow.getBlockPos();
		BlockPos.Mutable lvt_4_1_ = new BlockPos.Mutable();

		for (int lvt_5_1_ = -8; lvt_5_1_ <= 2; lvt_5_1_++) {
			for (int lvt_6_1_ = 0; lvt_6_1_ < lvt_1_1_; ++lvt_6_1_) {
				for (int lvt_7_1_ = 0; lvt_7_1_ <= lvt_6_1_; lvt_7_1_ = lvt_7_1_ > 0 ? -lvt_7_1_ : 1 - lvt_7_1_) {
					for (int lvt_8_1_ = lvt_7_1_ < lvt_6_1_ && lvt_7_1_ > -lvt_6_1_ ? lvt_6_1_ : 0; lvt_8_1_ <= lvt_6_1_; lvt_8_1_ = lvt_8_1_ > 0 ? -lvt_8_1_ : 1 - lvt_8_1_) {
						lvt_4_1_.set(lvt_3_1_, lvt_7_1_, lvt_5_1_ - 1, lvt_8_1_);
						if (this.isPumpkin(crow.world, lvt_4_1_)) {
							this.destinationBlock = lvt_4_1_;
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
