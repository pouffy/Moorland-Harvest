package com.pouffydev.moorland_harvest.registry;

import com.pouffydev.moorland_harvest.content.block.DampTurfBlock;
import com.pouffydev.moorland_harvest.content.block.DriedTurfBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import static com.pouffydev.moorland_harvest.Moorland.ID;

public class MoorlandBlocks {
	private static final Material turf = new Material.Builder(MapColor.BROWN).build();
	public static final DampTurfBlock dampTurf = new DampTurfBlock(QuiltBlockSettings.of(turf).strength(0.9f, 1.5f));
	public static final DriedTurfBlock driedTurf = new DriedTurfBlock(QuiltBlockSettings.of(turf).strength(0.9f, 1.5f));


	public static <T extends Block> void registerBlock(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(ID, name), block);
	}
	public static <T extends Item> void registerItem(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(ID, name), item);
	}
	public static void onInitializeBlocks() {
		registerBlock("damp_turf", dampTurf);
		registerBlock("dried_turf", driedTurf);
	}
	public static void onInitializeBlockItems() {
		registerItem("damp_turf", new BlockItem(MoorlandBlocks.dampTurf, new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
		registerItem("dried_turf", new BlockItem(MoorlandBlocks.driedTurf, new QuiltItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
	}
}
