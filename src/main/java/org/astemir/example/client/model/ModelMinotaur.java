package org.astemir.example.client.model;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.misc.AdvancedCubeRenderer;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.entity.EntityMinotaur;


public class ModelMinotaur extends AnimatedAdvancedModel<EntityMinotaur> {

	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPIMod.MOD_ID,"entity/minotaur.png");
	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"entity/minotaur.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPIMod.MOD_ID,"entity/minotaur.animation.json");

	public ModelMinotaur() {
		super(MODEL,ANIMATIONS);
	}

	@Override
	public void animate(EntityMinotaur animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
		AdvancedCubeRenderer head = getModelRenderer("head");
		if (head != null) {
			lookAt(head, headPitch, headYaw);
		}
	}

	@Override
	public ResourceLocation getTexture(EntityMinotaur minotaur) {
		return TEXTURE;
	}
}