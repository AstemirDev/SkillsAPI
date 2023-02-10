package org.astemir.example.client.render.minotaur;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.CodeGenerator;
import org.astemir.api.client.animation.InterpolationType;
import org.astemir.api.client.animation.SmoothnessType;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.client.model.AnimatedAdvancedModel;
import org.astemir.api.math.Vector2;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleMinotaur;


public class ModelExampleMinotaur extends AnimatedAdvancedModel<EntityExampleMinotaur> {

	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"entity/minotaur.png");
	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPI.MOD_ID,"entity/minotaur.geo.json");
	public static ResourceLocation ANIMATIONS = ResourceUtils.loadAnimation(SkillsAPI.MOD_ID,"entity/minotaur.animation.json");

	public ModelExampleMinotaur() {
		super(MODEL,ANIMATIONS);
		interpolation(InterpolationType.CATMULLROM);
		smoothnessType(SmoothnessType.EXPONENTIAL);
		addLayer(new ModelLayerExampleMinotaur(this));
	}

	@Override
	public void animate(EntityExampleMinotaur animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
		ModelElement head = getModelElement("head");
		if (head != null) {
			lookAt(head, headPitch, headYaw);
		}
	}

	@Override
	public ResourceLocation getTexture(EntityExampleMinotaur minotaur) {
		return TEXTURE;
	}
}