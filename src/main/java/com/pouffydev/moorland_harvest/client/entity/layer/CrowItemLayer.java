package com.pouffydev.moorland_harvest.client.entity.layer;

import com.pouffydev.moorland_harvest.client.entity.model.CrowModel;
import com.pouffydev.moorland_harvest.client.entity.renderer.CrowRenderer;
import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class CrowItemLayer extends FeatureRenderer<CrowEntity, CrowModel<CrowEntity>> {

	public CrowItemLayer(CrowRenderer renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CrowEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack itemstack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
		matrices.push();
		if(entity.isBaby()){
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0.0D, 1.5D, 0D);
		}
		matrices.push();
		translateToHand(matrices);
		matrices.translate(0, -0.09F, -0.125F);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-2.5F));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90F));
		matrices.scale(0.75F, 0.75F, 0.75F);
		HeldItemRenderer renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getHeldItemRenderer();
		renderer.renderItem(entity, itemstack, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light);
		matrices.pop();
		matrices.pop();
	}

	protected void translateToHand(MatrixStack matrixStack) {
		this.getContextModel().getBody().rotate(matrixStack);
		this.getContextModel().getHead().rotate(matrixStack);
	}
}
