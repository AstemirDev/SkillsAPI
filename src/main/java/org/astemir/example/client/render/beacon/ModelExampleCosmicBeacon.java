package org.astemir.example.client.render.beacon;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.block.BlockEntityExampleCosmicBeacon;


public class ModelExampleCosmicBeacon extends AnimatedAdvancedModel<BlockEntityExampleCosmicBeacon> {

	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"block/cosmic_beacon.png");
	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPI.MOD_ID,"block/cosmic_beacon.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPI.MOD_ID,"block/cosmic_beacon.animation.json");

	public ModelExampleCosmicBeacon() {
		super(MODEL,ANIMATIONS);
	}

	@Override
	public ResourceLocation getTexture(BlockEntityExampleCosmicBeacon target) {
		return TEXTURE;
	}
}