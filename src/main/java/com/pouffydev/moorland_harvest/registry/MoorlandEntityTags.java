package com.pouffydev.moorland_harvest.registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.pouffydev.moorland_harvest.Moorland.ID;

public class MoorlandEntityTags extends FabricTagProvider<EntityType<?>> {


	/**
	 * Construct a new {@link FabricTagProvider} with the default computed path.
	 *
	 * <p>Common implementations of this class are provided. For example @see BlockTagProvider
	 *
	 * @param dataGenerator The data generator instance
	 */
	public MoorlandEntityTags(FabricDataGenerator dataGenerator) {
		super(dataGenerator, Registry.ENTITY_TYPE);
	}

	// Entity Tag Keys
	public static final TagKey<EntityType<?>> DAMP_TURF_WALKABLE_MOBS = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(ID,"damp_turf_walkable"));
	public static final TagKey<EntityType<?>> SCATTERS_CROWS = TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(ID,"scatters_crows"));



	// Registering Entity Tags
	@Override
	protected void generateTags() {
		getOrCreateTagBuilder(DAMP_TURF_WALKABLE_MOBS)
			.add(EntityType.SLIME);
		getOrCreateTagBuilder(SCATTERS_CROWS)
			.add(EntityType.IRON_GOLEM)
			.add(EntityType.MINECART)
			.add(EntityType.CHEST_MINECART)
			.add(EntityType.COMMAND_BLOCK_MINECART)
			.add(EntityType.FURNACE_MINECART)
			.add(EntityType.HOPPER_MINECART)
			.add(EntityType.TNT_MINECART)
			.add(EntityType.TNT)
			.add(EntityType.FALLING_BLOCK)
			.add(EntityType.EGG)
			.add(EntityType.ENDER_PEARL)
			.add(EntityType.EXPERIENCE_BOTTLE)
			.add(EntityType.ARROW)
			.add(EntityType.SPECTRAL_ARROW)
			.add(EntityType.TRIDENT)
			.add(EntityType.FIREWORK_ROCKET)
			.add(EntityType.SMALL_FIREBALL)
			.add(EntityType.FIREBALL)
			.add(EntityType.DRAGON_FIREBALL)
			.add(EntityType.WITHER_SKULL)
			.add(EntityType.LLAMA_SPIT);
	}
}
