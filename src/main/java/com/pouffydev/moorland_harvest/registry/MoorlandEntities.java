package com.pouffydev.moorland_harvest.registry;

import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import com.pouffydev.moorland_harvest.content.entity.turf_roamer.TurfRoamer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class MoorlandEntities {
	public static final EntityType<TurfRoamer> turfRoamer = FabricEntityTypeBuilder.createMob()
		.entityFactory(TurfRoamer::new)
		.spawnGroup(SpawnGroup.MONSTER)
		.dimensions(EntityDimensions.fixed(1.0F, 1.0F))
		.trackRangeBlocks(32)
		.defaultAttributes(TurfRoamer::createTurfRoamerAttributes)
		.spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.WORLD_SURFACE, TurfRoamer::canRoamerSpawn)
		.build();

	public static final EntityType<CrowEntity> crow = FabricEntityTypeBuilder.createMob()
		.entityFactory(CrowEntity::new)
		.spawnGroup(SpawnGroup.CREATURE)
		.dimensions(EntityDimensions.fixed(0.45F, 0.45F))
		.trackRangeBlocks(32)
		.defaultAttributes(CrowEntity::createCrowAttributes)
		.spawnRestriction(SpawnRestriction.Location.ON_GROUND, Heightmap.Type.WORLD_SURFACE, CrowEntity::canCrowSpawn)
		.build();
	public static void onInitializeEntities() {
		Registry.register(Registry.ENTITY_TYPE, "moorland_harvest:turf_roamer", turfRoamer);
		Registry.register(Registry.ENTITY_TYPE, "moorland_harvest:crow", crow);
	}
}
