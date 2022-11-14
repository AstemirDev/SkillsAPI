package org.astemir.example.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.render.AdvancedCubeRenderer;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.ExampleAPIMod;
import org.astemir.example.common.entity.EntityMinotaur;


public class ModelMinotaur extends AdvancedModel<EntityMinotaur> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(ExampleAPIMod.MOD_ID,"entity/minotaur.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(ExampleAPIMod.MOD_ID,"entity/minotaur.animation.json");

	public ModelMinotaur() {
		super(RenderType::entityCutoutNoCull,MODEL,ANIMATIONS);
	}

	@Override
	public void animate(EntityMinotaur animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
		AdvancedCubeRenderer head = getModelRenderer("head");
		if (head != null) {
			lookAt(head, headPitch, headYaw);
		}
	}


}