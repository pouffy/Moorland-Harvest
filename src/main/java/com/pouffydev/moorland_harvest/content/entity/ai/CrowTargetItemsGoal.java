package com.pouffydev.moorland_harvest.content.entity.ai;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.MathHelper;

public class CrowTargetItemsGoal extends TargetItemsGoal {

	public CrowTargetItemsGoal(PathAwareEntity creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
		super(creature, ItemEntity.class, checkSight, onlyNearby, tickThreshold, radius);
		this.executionChance = 1;
	}

	public void stop() {
		super.stop();
		((CrowEntity) mob).aiItemFlag = false;
	}

	public boolean canStart() {
		return super.canStart()  &&  (mob.getTarget() == null || !mob.getTarget().isAlive());
	}

	public boolean shouldContinue() {
		return super.shouldContinue() && (mob.getTarget() == null || !mob.getTarget().isAlive());
	}

	@Override
	protected void moveTo() {
		CrowEntity crow = (CrowEntity) mob;
		if (this.targetEntity != null) {
			crow.aiItemFlag = true;
			if (this.mob.distanceTo(targetEntity) < 2) {
				crow.getNavigation().findPathTo(this.targetEntity.getX(), targetEntity.getY(), this.targetEntity.getZ(), 1);
				crow.peck();
			}
			if (this.mob.distanceTo(this.targetEntity) > 8 || crow.isFlying()) {
				crow.setFlying(true);

				if(!crow.canSee(targetEntity)){
					crow.getNavigation().findPathTo(this.targetEntity.getX(), 1 + crow.getY(), this.targetEntity.getZ(), 1);
				}else{
					final float f = (float) (crow.getX() - targetEntity.getX());
					final float f2 = (float) (crow.getZ() - targetEntity.getZ());
					final float xzDist = MathHelper.sqrt(f * f + f2 * f2);
					final float f1 = xzDist < 5F ? 0 : 1.8F;

					crow.getNavigation().findPathTo(this.targetEntity.getX(), f1 + this.targetEntity.getY(), this.targetEntity.getZ(), 1);
				}
			} else {
				this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		moveTo();
	}
}
