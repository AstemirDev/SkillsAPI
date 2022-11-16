package org.astemir.example.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.misc.AdvancedCubeRenderer;
import org.astemir.api.client.misc.RenderCall;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.block.BlockEntityCosmicBeacon;


public class ModelCosmicBeacon extends AnimatedAdvancedModel<BlockEntityCosmicBeacon> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"block/cosmic_beacon.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPIMod.MOD_ID,"block/cosmic_beacon.animation.json");

	public ModelCosmicBeacon() {
		super(RenderType::entityCutoutNoCull,MODEL,ANIMATIONS);
	}


	@Override
	public void animate(BlockEntityCosmicBeacon animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {

	}

	@Override
	public void onRenderModelCube(AdvancedCubeRenderer cube, PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall,int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

	}
}