package com.pouffydev.moorland_harvest;

import com.pouffydev.moorland_harvest.client.MoorlandEntityModelLayers;
import com.pouffydev.moorland_harvest.content.entity.crow.CrowRenderer;
import com.pouffydev.moorland_harvest.content.entity.turf_roamer.TurfRoamerRenderer;
import com.pouffydev.moorland_harvest.registry.MoorlandEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class MoorlandClient implements ClientModInitializer {
	public static void registerEntityRenderers() {
		EntityRendererRegistry.register(MoorlandEntities.turfRoamer, TurfRoamerRenderer::new);
		EntityRendererRegistry.register(MoorlandEntities.crow, CrowRenderer::new);
	}
	@Override
	public void onInitializeClient(ModContainer mod) {
		MoorlandEntityModelLayers.init();
		registerEntityRenderers();
	}
}
