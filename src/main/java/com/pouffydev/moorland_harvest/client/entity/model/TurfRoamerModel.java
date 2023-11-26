package com.pouffydev.moorland_harvest.client.entity.model;
// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class TurfRoamerModel<T extends Entity> extends SinglePartEntityModel<T> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightMiddleLeg;
	private final ModelPart leftMiddleLeg;
	private final ModelPart rightMiddleFrontLeg;
	private final ModelPart leftMiddleFrontLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	public TurfRoamerModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.rightHindLeg = root.getChild("rightHindLeg");
		this.leftHindLeg = root.getChild("leftHindLeg");
		this.rightMiddleLeg = root.getChild("rightMiddleLeg");
		this.leftMiddleLeg = root.getChild("leftMiddleLeg");
		this.rightMiddleFrontLeg = root.getChild("rightMiddleFrontLeg");
		this.leftMiddleFrontLeg = root.getChild("leftMiddleFrontLeg");
		this.rightFrontLeg = root.getChild("rightFrontLeg");
		this.leftFrontLeg = root.getChild("leftFrontLeg");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, -3.0F));
		modelPartData.addChild("rightHindLeg", ModelPartBuilder.create().uv(38, 0).cuboid(-11.5F, 1.1213F, -2.5F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 15.0F, 2.0F, 0.0F, 0.7854F, -0.7854F));
		modelPartData.addChild("leftHindLeg", ModelPartBuilder.create().uv(38, 0).cuboid(0.5F, 1.1213F, -2.5F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 15.0F, 2.0F, 0.0F, -0.7854F, 0.7854F));
		modelPartData.addChild("rightMiddleLeg", ModelPartBuilder.create().uv(38, 0).cuboid(-11.6621F, 1.4575F, -1.4454F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 15.0F, 1.0F, 0.0F, 0.2618F, -0.6109F));
		modelPartData.addChild("leftMiddleLeg", ModelPartBuilder.create().uv(38, 0).cuboid(0.6621F, 1.4575F, -1.4454F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 15.0F, 1.0F, 0.0F, -0.2618F, 0.6109F));
		modelPartData.addChild("rightMiddleFrontLeg", ModelPartBuilder.create().uv(38, 0).cuboid(-11.6621F, 1.4575F, -0.5546F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 15.0F, 0.0F, 0.0F, -0.2618F, -0.6109F));
		modelPartData.addChild("leftMiddleFrontLeg", ModelPartBuilder.create().uv(38, 0).cuboid(0.6621F, 1.4575F, -0.5546F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 15.0F, 0.0F, 0.0F, 0.2618F, 0.6109F));
		modelPartData.addChild("rightFrontLeg", ModelPartBuilder.create().uv(38, 0).cuboid(-11.5F, 1.1213F, 0.5F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 15.0F, -1.0F, 0.0F, -0.7854F, -0.7854F));
		modelPartData.addChild("leftFrontLeg", ModelPartBuilder.create().uv(38, 0).cuboid(0.5F, 1.1213F, 0.5F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 15.0F, -1.0F, 0.0F, 0.7854F, 0.7854F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	public ModelPart getPart() {
		return this.root;
	}
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		float f = 0.7853982F;
		this.rightHindLeg.roll = -0.7853982F;
		this.leftHindLeg.roll = 0.7853982F;
		this.rightMiddleLeg.roll = -0.58119464F;
		this.leftMiddleLeg.roll = 0.58119464F;
		this.rightMiddleFrontLeg.roll = -0.58119464F;
		this.leftMiddleFrontLeg.roll = 0.58119464F;
		this.rightFrontLeg.roll = -0.7853982F;
		this.leftFrontLeg.roll = 0.7853982F;
		float g = -0.0F;
		float h = 0.3926991F;
		this.rightHindLeg.yaw = 0.7853982F;
		this.leftHindLeg.yaw = -0.7853982F;
		this.rightMiddleLeg.yaw = 0.3926991F;
		this.leftMiddleLeg.yaw = -0.3926991F;
		this.rightMiddleFrontLeg.yaw = -0.3926991F;
		this.leftMiddleFrontLeg.yaw = 0.3926991F;
		this.rightFrontLeg.yaw = -0.7853982F;
		this.leftFrontLeg.yaw = 0.7853982F;
		float i = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbDistance;
		float j = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbDistance;
		float k = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbDistance;
		float l = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 4.712389F) * 0.4F) * limbDistance;
		float m = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 0.0F) * 0.4F) * limbDistance;
		float n = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 3.1415927F) * 0.4F) * limbDistance;
		float o = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 1.5707964F) * 0.4F) * limbDistance;
		float p = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 4.712389F) * 0.4F) * limbDistance;
		ModelPart var10000 = this.rightHindLeg;
		var10000.yaw += i;
		var10000 = this.leftHindLeg;
		var10000.yaw += -i;
		var10000 = this.rightMiddleLeg;
		var10000.yaw += j;
		var10000 = this.leftMiddleLeg;
		var10000.yaw += -j;
		var10000 = this.rightMiddleFrontLeg;
		var10000.yaw += k;
		var10000 = this.leftMiddleFrontLeg;
		var10000.yaw += -k;
		var10000 = this.rightFrontLeg;
		var10000.yaw += l;
		var10000 = this.leftFrontLeg;
		var10000.yaw += -l;
		var10000 = this.rightHindLeg;
		var10000.roll += m;
		var10000 = this.leftHindLeg;
		var10000.roll += -m;
		var10000 = this.rightMiddleLeg;
		var10000.roll += n;
		var10000 = this.leftMiddleLeg;
		var10000.roll += -n;
		var10000 = this.rightMiddleFrontLeg;
		var10000.roll += o;
		var10000 = this.leftMiddleFrontLeg;
		var10000.roll += -o;
		var10000 = this.rightFrontLeg;
		var10000.roll += p;
		var10000 = this.leftFrontLeg;
		var10000.roll += -p;
	}
}
