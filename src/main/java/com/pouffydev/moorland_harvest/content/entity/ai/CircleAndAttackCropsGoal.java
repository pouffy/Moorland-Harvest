package com.pouffydev.moorland_harvest.content.entity.ai;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import com.pouffydev.moorland_harvest.registry.MoorlandBlockTags;
import net.minecraft.block.CropBlock;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldView;

public class CircleAndAttackCropsGoal extends MoveToTargetPosGoal {

	private CrowEntity crow;
	private int idleAtFlowerTime = 0;
	private boolean isAboveDestinationBear;
	float circlingTime = 0;
	float circleDistance = 2;
	float maxCirclingTime = 80;
	float yLevel = 2;
	boolean clockwise = false;
	boolean circlePhase = false;

	public CircleAndAttackCropsGoal(CrowEntity bird) {
		super(bird, 1D, 32, 8);
		this.crow = bird;
	}

	public void start() {
		super.start();
		circlePhase = true;
		clockwise = crow.getRandom().nextBoolean();
		yLevel = 1 + crow.getRandom().nextInt(3);
		circleDistance = 1 + crow.getRandom().nextInt(3);
	}

	public boolean canStart() {
		return !crow.isBaby() && (crow.getTarget() == null || !crow.getTarget().isAlive()) && crow.fleePumpkinFlag == 0 && !crow.aiItemFlag && super.canStart();
	}

	public boolean shouldContinue() {
		return targetPos != null && (crow.getTarget() == null || !crow.getTarget().isAlive()) && !crow.aiItemFlag && crow.fleePumpkinFlag == 0 && super.shouldContinue();
	}

	public void stop() {
		idleAtFlowerTime = 0;
		circlingTime = 0;
		tryingTime = 0;
		targetPos = BlockPos.ORIGIN;
	}

	public double acceptedDistance() {
		return 1D;
	}

	public void tick() {
		if(targetPos == null){
			return;
		}
		BlockPos blockpos = this.getTargetPos();
		if(circlePhase){
			this.tryingTime = 0;
			BlockPos circlePos = getVultureCirclePos(blockpos);
			if (circlePos != null) {
				crow.setFlying(true);
				crow.getNavigation().startMovingTo(circlePos.getX() + 0.5D, circlePos.getY() + 0.5D, circlePos.getZ() + 0.5D, 0.7F);
			}
			circlingTime++;
			if(circlingTime > 200){
				circlingTime = 0;
				circlePhase = false;
			}
		}else{
			super.tick();
			if(crow.isOnGround()){
				crow.setFlying(false);
			}
			if (!isWithinXZDist(blockpos, this.mob.getPos(), this.acceptedDistance())) {
				this.isAboveDestinationBear = false;
				++this.tryingTime;
				this.mob.getNavigation().findPathTo((double) ((float) blockpos.getX()) + 0.5D, blockpos.getY() - 0.5D, (double) ((float) blockpos.getZ()) + 0.5D, 1);
			} else {
				this.isAboveDestinationBear = true;
				--this.tryingTime;
			}

			if (this.isReachedTarget()) {
				crow.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new Vec3d(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5));
				if (this.idleAtFlowerTime >= 5) {
					this.pollinate();
					this.stop();
				} else {
					crow.peck();
					++this.idleAtFlowerTime;
				}
			}
		}
	}

	public BlockPos getVultureCirclePos(BlockPos target) {
		float angle = (0.01745329251F * 8 * (clockwise ? -circlingTime : circlingTime));
		double extraX = circleDistance * MathHelper.sin((angle));
		double extraZ = circleDistance * MathHelper.cos(angle);
		BlockPos pos = new BlockPos(target.getX() + 0.5F + extraX, target.getY() + 1 + yLevel, target.getZ() + 0.5F + extraZ);
		if (crow.world.isSpaceEmpty(crow)) {
			return pos;
		}
		return null;
	}

	private boolean isWithinXZDist(BlockPos blockpos, Vec3d positionVec, double distance) {
		return blockpos.getSquaredDistance(new BlockPos(positionVec.x, blockpos.getY(), positionVec.z)) < distance * distance;
	}

	protected boolean isReachedTarget() {
		return this.isAboveDestinationBear;
	}

	private void pollinate() {
		if(crow.world.getBlockState(targetPos).getBlock() instanceof CropBlock){
			CropBlock block = (CropBlock)crow.world.getBlockState(targetPos).getBlock();
			int cropAge = crow.world.getBlockState(targetPos).get(block.getAgeProperty());
			if(cropAge > 0){
				crow.world.setBlockState(targetPos, crow.world.getBlockState(targetPos).with(block.getAgeProperty(), cropAge - 1));
			}else{
				crow.world.breakBlock(targetPos, true);
			}
			stop();
		}else{
			crow.world.breakBlock(targetPos, true);
			stop();
		}
		tryingTime = 1200;
	}

	@Override
	protected boolean isTargetPos(WorldView worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos).isIn(MoorlandBlockTags.CROW_FOOD_BLOCKS);
	}
}
