package com.pouffydev.moorland_harvest.client.entity.renderer;

import com.pouffydev.moorland_harvest.client.entity.MoorlandEntityModelLayers;
import com.pouffydev.moorland_harvest.client.entity.model.TurfRoamerModel;
import com.pouffydev.moorland_harvest.content.entity.turf_roamer.TurfRoamer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import static com.pouffydev.moorland_harvest.Moorland.ID;
@ClientOnly
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
