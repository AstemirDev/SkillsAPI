package org.astemir.example.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.misc.AdvancedCubeRenderer;
import org.astemir.api.client.misc.RenderCall;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.entity.EntitySharkBoat;


public class ModelSharkBoat extends AnimatedAdvancedModel<EntitySharkBoat> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"entity/shark_boat.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPIMod.MOD_ID,"entity/shark_boat.animation.json");

	public ModelSharkBoat() {
		super(RenderType::entityCutoutNoCull,MODEL,ANIMATIONS);
	}

	@Override
	public void renderModel(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
		stack.translate(0,-((EntitySharkBoat)getModelWrapper().getRenderTarget()).boatInWaterOffset.value(Minecraft.getInstance().getPartialTick()),0);
		super.renderModel(stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha, renderCall, resetBuffer);
	}

	@Override
	public void animate(EntitySharkBoat animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
	}

	@Override
	public void onRenderModelCube(AdvancedCubeRenderer cube, PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall,int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	}
}