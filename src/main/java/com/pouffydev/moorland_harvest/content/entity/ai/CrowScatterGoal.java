package com.pouffydev.moorland_harvest.content.entity.ai;

import com.google.common.base.Predicate;
import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import com.pouffydev.moorland_harvest.registry.MoorlandEntityTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class CrowScatterGoal extends Goal {
	private CrowEntity crow;
	protected final CrowScatterGoal.Sorter theNearestAttackableTargetSorter;
	protected final Predicate<? super Entity> targetEntitySelector;
	protected int executionChance = 8;
	protected boolean mustUpdate;
	private Entity targetEntity;
	private Vec3d flightTarget = null;
	private int cooldown = 0;

	public CrowScatterGoal(CrowEntity crow) {
		this.crow = crow;
		this.setControls(EnumSet.of(Goal.Control.MOVE));
		this.theNearestAttackableTargetSorter = new CrowScatterGoal.Sorter(crow);
		this.targetEntitySelector = new Predicate<Entity>() {
			@Override
			public boolean apply(@Nullable Entity e) {
				return e.isAlive() && e.getType().isIn(MoorlandEntityTags.SCATTERS_CROWS) || e instanceof PlayerEntity && !((PlayerEntity) e).isCreative();
			}
		};
	}

	@Override
	public boolean canStart() {
		if (!this.mustUpdate) {
			final long worldTime = crow.world.getTime() % 10;
			if (worldTime != 0) {
				if (crow.getRandom().nextInt(this.executionChance) != 0) {
					return false;
				}
			}
		}
		final List<Entity> list = crow.world.getEntitiesByClass(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
		if (list.isEmpty()) {
			return false;
		} else {
			list.sort(this.theNearestAttackableTargetSorter);
			this.targetEntity = list.get(0);
			this.mustUpdate = false;
			return true;
		}
	}

	@Override
	public boolean shouldContinue() {
		return targetEntity != null;
	}

	public void stop() {
		flightTarget = null;
		this.targetEntity = null;
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
		}
		if (flightTarget != null) {
			crow.setFlying(true);
			crow.getMoveControl().moveTo(flightTarget.x, flightTarget.y, flightTarget.z, 1F);
			if(cooldown == 0 && crow.isTargetBlocked(flightTarget)){
				cooldown = 30;
				flightTarget = null;
			}
		}

		if (targetEntity != null) {
			if (crow.isOnGround() || flightTarget == null || crow.squaredDistanceTo(flightTarget) < 3) {
				final Vec3d vec = crow.getBlockInViewAway(targetEntity.getPos(), 0);
				if (vec != null && vec.y > crow.getY()) {
					flightTarget = vec;
				}
			}
			if (crow.distanceTo(targetEntity) > 20.0F) {
				this.stop();
			}
		}
	}

	protected double getTargetDistance() {
		return 4D;
	}

	protected Box getTargetableArea(double targetDistance) {
		final Vec3d renderCenter = new Vec3d(crow.getX(), crow.getY() + 0.5, crow.getZ());
		final Box aabb = new Box(-2, -2, -2, 2, 2, 2);
		return aabb.offset(renderCenter);
	}


	public class Sorter implements Comparator<Entity> {
		private final Entity theEntity;

		public Sorter(Entity theEntityIn) {
			this.theEntity = theEntityIn;
		}

		public int compare(Entity entity, Entity entity1) {
			final double d0 = this.theEntity.squaredDistanceTo(entity);
			final double d1 = this.theEntity.squaredDistanceTo(entity1);
			return Double.compare(d0, d1);
		}
	}
}
