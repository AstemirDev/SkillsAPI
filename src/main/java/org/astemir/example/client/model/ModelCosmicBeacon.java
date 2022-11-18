package org.astemir.example.client.model;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.block.BlockEntityCosmicBeacon;


public class ModelCosmicBeacon extends AnimatedAdvancedModel<BlockEntityCosmicBeacon> {

	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPIMod.MOD_ID,"block/cosmic_beacon.png");
	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"block/cosmic_beacon.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPIMod.MOD_ID,"block/cosmic_beacon.animation.json");

	public ModelCosmicBeacon() {
		super(MODEL,ANIMATIONS);
	}

	@Override
	public ResourceLocation getTexture() {
		return TEXTURE;
	}
}