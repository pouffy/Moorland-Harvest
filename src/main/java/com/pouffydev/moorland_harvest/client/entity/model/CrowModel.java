package com.pouffydev.moorland_harvest.client.entity.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.pouffydev.moorland_harvest.content.entity.crow.CrowEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class CrowModel<T extends Entity> extends EntityModel<CrowEntity> {
	private final ModelPart body_main;
	private final ModelPart left_wing_main;
	private final ModelPart right_wing_main;
	private final ModelPart head_main;
	private final ModelPart tail;
	private final ModelPart left_leg_main;
	private final ModelPart right_leg_main;
	public CrowModel(ModelPart root) {
		this.body_main = root.getChild("body_main");
		this.head_main = root.getChild("head_main");
		this.tail = root.getChild("tail");
		this.left_leg_main = root.getChild("left_leg_main");
		this.right_leg_main = root.getChild("right_leg_main");
		this.left_wing_main = body_main.getChild("left_wing_main");
		this.right_wing_main = body_main.getChild("right_wing_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body_main = modelPartData.addChild("body_main", ModelPartBuilder.create().uv(3, 9).cuboid(-1.5F, 0.0F, -0.5F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 20.5F, -3.0F, 1.1345F, 0.0F, 0.0F));
		body_main.addChild("left_wing_main", ModelPartBuilder.create().uv(20, 9).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 2.0F, new Dilation(0.0F))
			.uv(6, 17).cuboid(0.25F, 4.25F, -0.75F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 0.4F, 0.2F));
		body_main.addChild("right_wing_main", ModelPartBuilder.create().uv(20, 9).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 2.0F, new Dilation(0.0F))
			.uv(6, 17).cuboid(-0.25F, 4.25F, -0.75F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 0.4F, 0.2F));
		modelPartData.addChild("head_main", ModelPartBuilder.create().uv(1, 1).cuboid(-1.0F, -1.5F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
			.uv(15, 7).cuboid(-0.5F, -0.75F, -2.9F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 19.7F, -2.8F));
		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create(), ModelTransform.of(0.0F, 21.1F, 1.2F, 0.8727F, 0.0F, 0.0F));
		tail.addChild("right_feather", ModelPartBuilder.create().uv(2, 17).cuboid(-1.5F, -1.25F, -2.25F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.9F, -1.2F, -2.9183F, 0.2129F, 0.0479F));
		tail.addChild("left_feather", ModelPartBuilder.create().uv(2, 17).cuboid(1.5F, -1.0F, -2.25F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.9F, -1.2F, -2.9167F, -0.2086F, -0.0557F));
		tail.addChild("left_inner_feather", ModelPartBuilder.create().uv(3, 20).cuboid(-1.5F, -0.9F, -0.5F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
			.uv(3, 20).mirrored().cuboid(-1.5F, -0.9F, 0.5F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.9F, -1.2F, -1.5708F, 1.4835F, 1.5708F));
		modelPartData.addChild("left_leg_main", ModelPartBuilder.create().uv(14, 18).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 23.0F, 0.5F));
		modelPartData.addChild("right_leg_main", ModelPartBuilder.create().uv(14, 18).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 23.0F, 0.5F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	public void setAngles(CrowEntity crowEntity, float f, float g, float h, float i, float j) {
		this.setAngles(getPose(crowEntity), f, g, h, i, j);
	}

	public void animateModel(CrowEntity crowEntity, float f, float g, float h) {
		this.animateModel(getPose(crowEntity));
	}

	public ModelPart getBody() {
		return body_main;
	}
	public ModelPart getHead() {
		return left_wing_main;
	}

	private void setAngles(CrowModel.Pose pose, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
		this.head_main.pitch = headPitch * (float) (Math.PI / 180.0);
		this.head_main.yaw = headYaw * (float) (Math.PI / 180.0);
		this.head_main.roll = 0.0F;
		this.head_main.pivotX = 0.0F;
		this.body_main.pivotX = 0.0F;
		this.tail.pivotX = 0.0F;
		this.right_wing_main.pivotX = -1.5F;
		this.left_wing_main.pivotX = 1.5F;
		switch(pose) {
			case STANDING:
				this.left_leg_main.pitch += MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
				this.right_leg_main.pitch += MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
			case FLYING:
			default:
				float var10 = age * 0.3F;
				this.head_main.pivotY = 15.69F + var10;
				this.tail.pitch = 1.015F + MathHelper.cos(limbAngle * 0.6662F) * 0.3F * limbDistance;
				this.tail.pivotY = 21.07F + var10;
				this.body_main.pivotY = 16.5F + var10;
				this.left_wing_main.roll = -0.0873F - age;
				this.left_wing_main.pivotY = 16.94F + var10;
				this.right_wing_main.roll = 0.0873F + age;
				this.right_wing_main.pivotY = 16.94F + var10;
				this.left_leg_main.pivotY = 22.0F + var10;
				this.right_leg_main.pivotY = 22.0F + var10;
		}
	}
	private void animateModel(CrowModel.Pose pose) {
		this.tail.pitch = -0.2214F;
		this.body_main.pitch = 0.4937F;
		this.left_wing_main.pitch = -0.6981F;
		this.left_wing_main.yaw = (float) -Math.PI;
		this.right_wing_main.pitch = -0.6981F;
		this.right_wing_main.yaw = (float) -Math.PI;
		this.left_leg_main.pitch = -0.0299F;
		this.right_leg_main.pitch = -0.0299F;
		this.left_leg_main.pivotY = 22.0F;
		this.right_leg_main.pivotY = 22.0F;
		this.left_leg_main.roll = 0.0F;
		this.right_leg_main.roll = 0.0F;
		switch(pose) {
			case STANDING:
			default:
				break;
			case FLYING:
				this.left_leg_main.pitch += (float) (Math.PI * 2.0 / 9.0);
				this.right_leg_main.pitch += (float) (Math.PI * 2.0 / 9.0);
		}
	}
	private static CrowModel.Pose getPose(CrowEntity crow) {
		return crow.isFlying() ? CrowModel.Pose.FLYING : CrowModel.Pose.STANDING;
	}
	@ClientOnly
	public static enum Pose {
		FLYING,
		STANDING;
		private Pose() {
		}
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		left_wing_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_wing_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		tail.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_leg_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		head_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
