package com.pouffydev.moorland_harvest.content.entity.ai;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CrowMeleeGoal extends Goal {
	private CrowEntity crow;
	float circlingTime = 0;
	float circleDistance = 1;
	float yLevel = 2;
	boolean clockwise = false;
	private int maxCircleTime;

	public CrowMeleeGoal(CrowEntity crow) {
		this.crow = crow;
	}

	public boolean canStart(){
		return crow.getTarget() != null;
	}

	public void start() {
		clockwise = crow.getRandom().nextBoolean();
		yLevel = crow.getRandom().nextInt(2);
		circlingTime = 0;
		maxCircleTime = 20 + crow.getRandom().nextInt(100);
		circleDistance = 1F + crow.getRandom().nextFloat() * 3F;
	}

	public void stop() {
		clockwise = crow.getRandom().nextBoolean();
		yLevel = crow.getRandom().nextInt(2);
		circlingTime = 0;
		maxCircleTime = 20 + crow.getRandom().nextInt(100);
		circleDistance = 1F + crow.getRandom().nextFloat() * 3F;
		if(crow.isOnGround()){
			crow.setFlying(false);
		}
	}

	public void tick() {
		if (this.crow.isFlying()) {
			circlingTime++;
		}
		LivingEntity target = crow.getTarget();
		if(circlingTime > maxCircleTime){
			crow.getNavigation().startMovingTo(target.getX(), target.getY() + target.getEyeY() / 2F, target.getZ(), 1.3F);
			if(crow.distanceTo(target) < 2){
				crow.peck();
				if(target.getGroup() == EntityGroup.UNDEAD){
					target.damage(DamageSource.MAGIC, 4);
				}else{
					target.damage(DamageSource.GENERIC, 1);
				}

				stop();
			}
		}else{
			Vec3d circlePos = getVultureCirclePos(target.getPos());
			if (circlePos == null) {
				circlePos = target.getPos();
			}
			crow.setFlying(true);
			crow.getNavigation().startMovingTo(circlePos.x, circlePos.y + target.getEyeY() + 0.2F, circlePos.z, 1F);

		}
	}

	public Vec3d getVultureCirclePos(Vec3d target) {
		float angle = (0.01745329251F * 8 * (clockwise ? -circlingTime : circlingTime));
		double extraX = circleDistance * MathHelper.sin((angle));
		double extraZ = circleDistance * MathHelper.cos(angle);
		Vec3d pos = new Vec3d(target.x + extraX, target.y + yLevel, target.z + extraZ);
		if (crow.world.isAir(new BlockPos(pos))) {
			return pos;
		}
		return null;
	}
}
