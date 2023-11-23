package com.pouffydev.moorland_harvest.registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.pouffydev.moorland_harvest.Moorland.ID;

public class MoorlandBlockTags extends FabricTagProvider<Block> {
	/**
	 * Construct a new {@link FabricTagProvider} with the default computed path.
	 *
	 * <p>Common implementations of this class are provided. For example @see BlockTagProvider
	 *
	 * @param dataGenerator The data generator instance
	 * @param registry      The backing registry for the Tag type.
	 */
	public MoorlandBlockTags(FabricDataGenerator dataGenerator) {
		super(dataGenerator, Registry.BLOCK);
	}
	public static final TagKey<Block> CROW_FEARS = TagKey.of(Registry.BLOCK_KEY, new Identifier(ID,"crow_fears"));
	public static final TagKey<Block> CROW_FOOD_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier(ID,"crow_food_blocks"));
	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(CROW_FEARS)
			.add(Blocks.PUMPKIN)
			.add(Blocks.CARVED_PUMPKIN)
			.add(Blocks.JACK_O_LANTERN)
			.add(Blocks.MELON);
		getOrCreateTagBuilder(CROW_FOOD_BLOCKS)
			.add(Blocks.WHEAT)
			.add(Blocks.POTATOES)
			.add(Blocks.CARROTS)
			.add(BlockTags.SAPLINGS.id())
			.add(Blocks.BEETROOTS);
	}
}
