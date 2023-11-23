package com.pouffydev.moorland_harvest.content.entity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ITargetsDroppedItems {
	boolean canTargetItem(ItemStack stack);

	void onGetItem(ItemEntity e);

	default void onFindTarget(ItemEntity e){}

	default double getMaxDistToItem(){return 2.0D; }

	default void setItemFlag(boolean itemAIFlag){}

	default void peck(){}

	default void setFlying(boolean flying){}

	default boolean isFlying(){ return false; }
}
