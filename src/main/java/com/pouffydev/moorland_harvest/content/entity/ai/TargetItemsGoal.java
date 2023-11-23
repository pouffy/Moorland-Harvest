package com.pouffydev.moorland_harvest.content.entity.ai;

import com.google.common.base.Predicate;
import com.pouffydev.moorland_harvest.content.entity.ITargetsDroppedItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class TargetItemsGoal<T extends ItemEntity> extends TargetGoal {
	protected final TargetItemsGoal.Sorter theNearestAttackableTargetSorter;
	protected final Predicate<? super ItemEntity> targetEntitySelector;
	protected int executionChance;
	protected boolean mustUpdate;
	protected ItemEntity targetEntity;
	protected ITargetsDroppedItems hunter;
	private int tickThreshold;
	private float radius = 9F;
	private int walkCooldown = 0;

	public TargetItemsGoal(PathAwareEntity creature, Class<T> targetClass, boolean checkSight) {
		this(creature, targetClass, checkSight, false);
		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}

	public TargetItemsGoal(PathAwareEntity creature, Class<T> targetClass, boolean checkSight, int tickThreshold) {
		this(creature, targetClass, checkSight, false, tickThreshold, 9);
		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}


	public TargetItemsGoal(PathAwareEntity creature, Class<T> targetClass, boolean checkSight, boolean onlyNearby) {
		this(creature, targetClass, 10, checkSight, onlyNearby, null, 0);
	}

	public TargetItemsGoal(PathAwareEntity creature, Class<T> targetClass, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
		this(creature, targetClass, 10, checkSight, onlyNearby, null, tickThreshold);
		this.radius = radius;
	}


	public TargetItemsGoal(PathAwareEntity creature, Class<T> targetClass, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector, int ticksExisted) {
		super(creature, targetClass, checkSight, onlyNearby);
		this.executionChance = chance;
		this.tickThreshold = ticksExisted;
		this.hunter = (ITargetsDroppedItems) creature;
		this.theNearestAttackableTargetSorter = new TargetItemsGoal.Sorter(creature);
		this.targetEntitySelector = new Predicate<ItemEntity>() {
			@Override
			public boolean apply(@Nullable ItemEntity item) {
				ItemStack stack = item.getStack();
				return !stack.isEmpty()  && hunter.canTargetItem(stack) && item.age > tickThreshold;
			}
		};
		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}

	@Override
	public boolean canStart() {
		if(!mob.getStackInHand(Hand.MAIN_HAND).isEmpty()){
			return false;
		}
		if (!this.mustUpdate) {
			long worldTime = this.mob.world.getTime() % 10;
			if (this.mob.getAir() >= 100 && worldTime != 0) {
				return false;
			}
			if (this.mob.getRandom().nextInt(this.executionChance) != 0 && worldTime != 0) {
				return false;
			}
		}
		List<ItemEntity> list = this.mob.world.getEntitiesByClass(ItemEntity.class, this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);
		if (list.isEmpty()) {
			return false;
		} else {
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			this.targetEntity = list.get(0);
			this.mustUpdate = false;
			this.hunter.onFindTarget(targetEntity);
			return true;
		}
	}

	protected double getFollowDistance() {
		return 16D;
	}


	protected Box getTargetableArea(double targetDistance) {
		Vec3d renderCenter = new Vec3d(this.mob.getX() + 0.5, this.mob.getY()+ 0.5, this.mob.getZ() + 0.5D);
		Box aabb = new Box(-radius, -radius, -radius, radius, radius, radius);
		return aabb.offset(renderCenter);
	}

	@Override
	public void start() {
		moveTo();
		super.start();
	}

	protected void moveTo(){
		if(walkCooldown > 0){
			walkCooldown--;
		}else{
			this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1);
			walkCooldown = 30 + this.mob.getRandom().nextInt(40);
		}
	}

	public void stop() {
		super.stop();
		this.mob.getNavigation().stop();
		this.targetEntity = null;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.targetEntity == null || this.targetEntity != null && !this.targetEntity.isAlive()) {
			this.stop();
			this.mob.getNavigation().stop();
		}else{
			moveTo();
		}
		if(targetEntity != null && this.mob.canSee(targetEntity) && this.mob.getWidth() > 2D && this.mob.isOnGround()){
			this.mob.getMoveControl().moveTo(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), 1);
		}
		if (this.targetEntity != null && this.targetEntity.isAlive() && this.mob.squaredDistanceTo(this.targetEntity) < this.hunter.getMaxDistToItem() && mob.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
			hunter.onGetItem(targetEntity);
			this.targetEntity.getStack().decrement(1);
			stop();
		}
	}

	public void makeUpdate() {
		this.mustUpdate = true;
	}

	@Override
	public boolean shouldContinue() {
		boolean path = this.mob.getWidth() > 2D ||  !this.mob.getNavigation().isIdle();
		return path && targetEntity != null && targetEntity.isAlive();
	}

	public static class Sorter implements Comparator<Entity> {
		private final Entity theEntity;

		public Sorter(Entity theEntityIn) {
			this.theEntity = theEntityIn;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			double d0 = this.theEntity.squaredDistanceTo(p_compare_1_);
			double d1 = this.theEntity.squaredDistanceTo(p_compare_2_);
			return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
		}
	}
}
