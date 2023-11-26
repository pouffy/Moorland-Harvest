package com.pouffydev.moorland_harvest.client;

import com.pouffydev.moorland_harvest.client.entity.MoorlandEntityModelLayers;
import com.pouffydev.moorland_harvest.client.entity.renderer.CrowRenderer;
import com.pouffydev.moorland_harvest.client.entity.renderer.TurfRoamerRenderer;
import com.pouffydev.moorland_harvest.registry.MoorlandEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@ClientOnly
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
