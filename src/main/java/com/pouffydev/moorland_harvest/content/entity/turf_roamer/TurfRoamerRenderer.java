package com.pouffydev.moorland_harvest.content.entity.turf_roamer;

import com.pouffydev.moorland_harvest.client.MoorlandEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static com.pouffydev.moorland_harvest.Moorland.ID;
@Environment(EnvType.CLIENT)
public class TurfRoamerRenderer extends MobEntityRenderer<TurfRoamer, TurfRoamerModel<TurfRoamer>> {
	private static final Identifier TEXTURE = new Identifier(ID, "textures/entity/moor_stalker.png");


	public TurfRoamerRenderer(EntityRendererFactory.Context context) {
		super(context, new TurfRoamerModel<>(context.getPart(MoorlandEntityModelLayers.moorStalker)), 0.8F);
	}

	protected float getLyingAngle(TurfRoamer turfRoamer) {
		return 180.0F;
	}

	public Identifier getTexture(TurfRoamer turfRoamer) {
		return TEXTURE;
	}
}
