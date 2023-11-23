package com.pouffydev.moorland_harvest.client;

import com.pouffydev.moorland_harvest.Moorland;
import com.pouffydev.moorland_harvest.content.entity.crow.CrowModel;
import com.pouffydev.moorland_harvest.content.entity.turf_roamer.TurfRoamerModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
@Environment(EnvType.CLIENT)
public class MoorlandEntityModelLayers {
	public static final EntityModelLayer moorStalker = createModelLayer("moor_stalker");
	public static final EntityModelLayer crow = createModelLayerInnerArmor("crow");

	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(moorStalker, TurfRoamerModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(crow, CrowModel::getTexturedModelData);
	}
	private static EntityModelLayer createModelLayer(String name) {
		return new EntityModelLayer(Moorland.asResource(name), "main");
	}

	private static EntityModelLayer createModelLayer(String name, String layer) {
		return new EntityModelLayer(Moorland.asResource(name), layer);
	}

	private static EntityModelLayer createModelLayerInnerArmor(String name) {
		return createModelLayer(name, "inner_armor");
	}

	private static EntityModelLayer createModelLayerOuterArmor(String name) {
		return createModelLayer(name, "outer_armor");
	}
}
