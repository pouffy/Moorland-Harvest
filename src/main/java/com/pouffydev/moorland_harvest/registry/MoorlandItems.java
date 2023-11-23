package com.pouffydev.moorland_harvest.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.pouffydev.moorland_harvest.Moorland.ID;

public class MoorlandItems {

	public static <T extends Item> void registerItem(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(ID, name), item);
	}
	public static void onInitializeItems() {
		registerItem("moor_stalker_spawn_egg", new SpawnEggItem(MoorlandEntities.turfRoamer, 0x483930, 0xaea195, new Item.Settings().group(ItemGroup.MISC)));
		registerItem("crow_spawn_egg", new SpawnEggItem(MoorlandEntities.crow, 0x20194c, 0x222222, new Item.Settings().group(ItemGroup.MISC)));
	}
}
