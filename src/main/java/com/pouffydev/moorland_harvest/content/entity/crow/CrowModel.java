package com.pouffydev.moorland_harvest.content.entity.crow;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CrowModel<T extends Entity> extends EntityModel<CrowEntity> {
	private final ModelPart left_wing_main;
	private final ModelPart left_wing;
	private final ModelPart right_wing_main;
	private final ModelPart right_wing;
	private final ModelPart tail_feathers;
	private final ModelPart right_inner_feather;
	private final ModelPart left_inner_feather;
	private final ModelPart right_feather;
	private final ModelPart left_feather;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart head_main;
	private final ModelPart head;
	private final ModelPart beak;
	private final ModelPart body_main;
	private final ModelPart body;
	public CrowModel(ModelPart root) {
		this.left_wing_main = root.getChild("left_wing_main");
		this.right_wing_main = root.getChild("right_wing_main");
		this.tail_feathers = root.getChild("tail_feathers");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.head_main = root.getChild("head_main");
		this.body_main = root.getChild("body_main");
		this.head = head_main.getChild("head");
		this.beak = head_main.getChild("beak");
		this.body = body_main.getChild("body");
		this.left_wing = left_wing_main.getChild("left_wing");
		this.right_wing = right_wing_main.getChild("right_wing");
		this.right_inner_feather = tail_feathers.getChild("right_inner_feather");
		this.left_inner_feather = tail_feathers.getChild("left_inner_feather");
		this.right_feather = tail_feathers.getChild("right_feather");
		this.left_feather = tail_feathers.getChild("left_feather");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData left_wing_main = modelPartData.addChild("left_wing_main", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, 20.75F, -3.65F));

		ModelPartData left_wing = left_wing_main.addChild("left_wing", ModelPartBuilder.create().uv(0, 8).cuboid(1.0F, -4.75F, 1.75F, 1.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 4.25F, 3.65F, 1.2654F, 0.0F, 0.0F));

		ModelPartData right_wing_main = modelPartData.addChild("right_wing_main", ModelPartBuilder.create(), ModelTransform.pivot(-1.5F, 20.75F, -3.65F));

		ModelPartData right_wing = right_wing_main.addChild("right_wing", ModelPartBuilder.create().uv(0, 8).cuboid(-2.0F, -4.75F, 1.75F, 1.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 4.25F, 3.65F, 1.2654F, 0.0F, 0.0F));

		ModelPartData tail_feathers = modelPartData.addChild("tail_feathers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.0F, 1.0F));

		ModelPartData right_inner_feather = tail_feathers.addChild("right_inner_feather", ModelPartBuilder.create().uv(13, 7).mirrored().cuboid(-2.5F, -2.9F, 0.5F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
			.uv(14, 7).cuboid(-2.5F, -2.9F, -0.5F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -1.0F, -1.5708F, 0.4363F, 1.5708F));

		ModelPartData left_inner_feather = tail_feathers.addChild("left_inner_feather", ModelPartBuilder.create().uv(13, 7).cuboid(-2.5F, -2.9F, -0.5F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F))
			.uv(14, 7).mirrored().cuboid(-2.5F, -2.9F, 0.5F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, -1.0F, -1.5708F, -0.4363F, -1.5708F));

		ModelPartData right_feather = tail_feathers.addChild("right_feather", ModelPartBuilder.create().uv(14, 5).cuboid(-1.5F, -3.0F, -3.75F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, -1.0F, -1.8694F, 0.0651F, 0.2084F));

		ModelPartData left_feather = tail_feathers.addChild("left_feather", ModelPartBuilder.create().uv(14, 5).cuboid(1.5F, -3.0F, -3.75F, 0.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, -1.0F, -1.8694F, -0.0564F, -0.2084F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(5, 16).cuboid(0.5F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(5, 16).mirrored().cuboid(-1.5F, -2.0F, -1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head_main = modelPartData.addChild("head_main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 20.0F, -3.0F));

		ModelPartData head = head_main.addChild("head", ModelPartBuilder.create().uv(22, 0).cuboid(-1.0F, 1.8F, 4.2F, 2.0F, 2.0F,2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 5.0F, 3.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData beak = head_main.addChild("beak", ModelPartBuilder.create().uv(0, 16).cuboid(-0.5F, -6.8F, 4.3F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
			.uv(11, 0).cuboid(-1.0F, -4.8F, 4.2F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 5.0F, 3.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData body_main = modelPartData.addChild("body_main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, -1.0F));

		ModelPartData body = body_main.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, 0.0F, -4.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.0F, 1.0F, -1.8762F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	public void setCoolerAngles(CrowEntity crow, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.setupAngles(getPose(crow), limbAngle, limbDistance, animationProgress, headYaw, headPitch);
	}
	public void animateCoolerModel(CrowEntity crow) {
		this.animateModel(getPose(crow));
	}
	private void setupAngles(CrowModel.Pose pose, float limbAngle, float limbDistance, float age, float headYaw, float headPitch) {
		this.head_main.pitch = headPitch * (float) (Math.PI / 180.0);
		this.head_main.yaw = headYaw * (float) (Math.PI / 180.0);
		this.head_main.roll = 0.0F;
		this.head_main.pivotX = 0.0F;
		this.body_main.pivotX = 0.0F;
		this.tail_feathers.pivotX = 0.0F;
		this.right_wing_main.pivotX = -1.5F;
		this.left_wing_main.pivotX = 1.5F;
		switch(pose) {
			case STANDING:
				this.left_leg.pitch += MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
				this.right_leg.pitch += MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
			case FLYING:
			default:
				float var10 = age * 0.3F;
				this.head_main.pivotY = 15.69F + var10;
				this.tail_feathers.pitch = 1.015F + MathHelper.cos(limbAngle * 0.6662F) * 0.3F * limbDistance;
				this.tail_feathers.pivotY = 21.07F + var10;
				this.body_main.pivotY = 16.5F + var10;
				this.left_wing_main.roll = -0.0873F - age;
				this.left_wing_main.pivotY = 16.94F + var10;
				this.right_wing_main.roll = 0.0873F + age;
				this.right_wing_main.pivotY = 16.94F + var10;
				this.left_leg.pivotY = 22.0F + var10;
				this.right_leg.pivotY = 22.0F + var10;
		}
	}
	private void animateModel(CrowModel.Pose pose) {
		this.tail_feathers.pitch = -0.2214F;
		this.body.pitch = 0.4937F;
		this.left_wing_main.pitch = -0.6981F;
		this.left_wing_main.yaw = (float) -Math.PI;
		this.right_wing_main.pitch = -0.6981F;
		this.right_wing_main.yaw = (float) -Math.PI;
		this.left_leg.pitch = -0.0299F;
		this.right_leg.pitch = -0.0299F;
		this.left_leg.pivotY = 22.0F;
		this.right_leg.pivotY = 22.0F;
		this.left_leg.roll = 0.0F;
		this.right_leg.roll = 0.0F;
		switch(pose) {
			case STANDING:
			default:
				break;
			case FLYING:
				this.left_leg.pitch += (float) (Math.PI * 2.0 / 9.0);
				this.right_leg.pitch += (float) (Math.PI * 2.0 / 9.0);
		}
	}
	private static CrowModel.Pose getPose(CrowEntity crow) {
		return crow.isFlying() ? CrowModel.Pose.FLYING : CrowModel.Pose.STANDING;
	}
	public static enum Pose {
		FLYING,
		STANDING;
		// $FF: synthetic method
		private static CrowModel.Pose[] poses() {
			return new CrowModel.Pose[]{FLYING, STANDING};
		}
	}
	@Override
	public void animateModel(CrowEntity entity, float limbAngle, float limbDistance, float tickDelta) {
		this.animateCoolerModel(entity);
	}
	@Override
	public void setAngles(CrowEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.setCoolerAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		left_wing_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_wing_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		tail_feathers.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		head_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
