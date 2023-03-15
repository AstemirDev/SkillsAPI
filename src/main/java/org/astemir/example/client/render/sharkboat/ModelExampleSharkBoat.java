package org.astemir.example.client.render.sharkboat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.client.model.SunAnimatedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleSharkBoat;


public class ModelExampleSharkBoat extends SunAnimatedModel<EntityExampleSharkBoat, IDisplayArgument> {

	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID, "entity/shark_boat.png");
	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPI.MOD_ID,"entity/shark_boat.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPI.MOD_ID,"entity/shark_boat.animation.json");

	public ModelExampleSharkBoat() {
		super(MODEL,ANIMATIONS);
	}

	@Override
	public void renderModel(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
		stack.translate(0,-((EntityExampleSharkBoat)getModelWrapper().getRenderTarget()).boatInWaterOffset.value(Minecraft.getInstance().getPartialTick()),0);
		super.renderModel(stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha, renderCall, resetBuffer);
	}

	@Override
	public ResourceLocation getTexture(EntityExampleSharkBoat sharkBoat) {
		return TEXTURE;
	}
}