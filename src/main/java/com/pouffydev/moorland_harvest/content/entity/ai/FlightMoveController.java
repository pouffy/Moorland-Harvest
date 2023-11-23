package com.pouffydev.moorland_harvest.content.entity.ai;

import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FlightMoveController extends MoveControl {
	private final MobEntity parentEntity;
	private final float speedGeneral;
	private final boolean shouldLookAtTarget;
	private final boolean needsYSupport;


	public FlightMoveController(MobEntity bird, float speedGeneral, boolean shouldLookAtTarget, boolean needsYSupport) {
		super(bird);
		this.parentEntity = bird;
		this.shouldLookAtTarget = shouldLookAtTarget;
		this.speedGeneral = speedGeneral;
		this.needsYSupport = needsYSupport;
	}

	public FlightMoveController(MobEntity bird, float speedGeneral, boolean shouldLookAtTarget) {
		this(bird, speedGeneral, shouldLookAtTarget, false);
	}

	public FlightMoveController(MobEntity bird, float speedGeneral) {
		this(bird, speedGeneral, true);
	}
	public void tick() {
		if (this.state == MoveControl.State.MOVE_TO) {
			Vec3d vector3d = new Vec3d(this.targetX - parentEntity.getX(), this.targetY - parentEntity.getY(), this.targetZ - parentEntity.getZ());
			double d0 = vector3d.length();
			if (d0 < parentEntity.getBoundingBox().getAverageSideLength()) {
				this.state = MoveControl.State.WAIT;
				parentEntity.setVelocity(parentEntity.getVelocity().multiply(0.5D));
			} else {
				parentEntity.setVelocity(parentEntity.getVelocity().add(vector3d.multiply(this.speed * speedGeneral * 0.05D / d0)));
				if (needsYSupport) {
					double d1 = this.targetY - parentEntity.getY();
					parentEntity.move(MovementType.SELF, parentEntity.getVelocity().add(0.0D, parentEntity.getMovementSpeed() * speedGeneral * MathHelper.clamp(d1, -1, 1) * 0.6F, 0.0D));
				}
				if (parentEntity.getTarget() == null || !shouldLookAtTarget) {
					Vec3d vector3d1 = parentEntity.getVelocity();
					parentEntity.setYaw(-((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * MathHelper.RADIANS_PER_DEGREE);
					parentEntity.bodyYaw = parentEntity.getYaw();
				} else {
					double d2 = parentEntity.getTarget().getX() - parentEntity.getX();
					double d1 = parentEntity.getTarget().getZ() - parentEntity.getZ();
					parentEntity.setYaw(-((float) MathHelper.atan2(d2, d1)) * MathHelper.RADIANS_PER_DEGREE);
					parentEntity.bodyYaw = parentEntity.getYaw();
				}
			}

		} else if (this.state == State.STRAFE) {
			this.state = State.WAIT;
		}
	}
	private boolean canReach(Vec3d vec3d, int speedGeneral) {
		Box axisalignedbb = this.parentEntity.getBoundingBox();

		for (int i = 1; i < speedGeneral; ++i) {
			axisalignedbb = axisalignedbb.offset(vec3d);
			if (!this.parentEntity.getWorld().isSpaceEmpty(this.parentEntity, axisalignedbb)) {
				return false;
			}
		}

		return true;
	}
}
