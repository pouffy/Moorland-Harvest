package com.pouffydev.moorland_harvest.content.entity.crow;

import com.pouffydev.moorland_harvest.client.MoorlandEntityModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static com.pouffydev.moorland_harvest.Moorland.ID;

public class CrowRenderer extends MobEntityRenderer<CrowEntity, CrowModel<CrowEntity>> {
	private static final Identifier TEXTURE = new Identifier(ID, "textures/entity/crow.png");
	public CrowRenderer(EntityRendererFactory.Context context) {
		super(context, new CrowModel<>(context.getPart(MoorlandEntityModelLayers.crow)), 0.3F);
	}
	public float getAnimationProgress(CrowEntity crow, float f) {
		float g = MathHelper.lerp(f, crow.prevFlyProgress, crow.flyProgress);
		float h = MathHelper.lerp(f, crow.prevMaxWingDeviation, crow.maxWingDeviation);
		return (MathHelper.sin(g) + 1.0F) * h;
	}
	@Override
	public Identifier getTexture(CrowEntity entity) {
		return TEXTURE;
	}
}
