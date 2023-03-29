package org.astemir.example.client.render.minotaur;

import com.lowdragmc.shimmer.client.ShimmerRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.render.RenderCall;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.animation.InterpolationType;
import org.astemir.api.client.animation.SmoothnessType;
import org.astemir.api.client.render.cube.ModelElement;
import org.astemir.api.client.model.SkillsAnimatedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleMinotaur;



public class ModelExampleMinotaur extends SkillsAnimatedModel<EntityExampleMinotaur,IDisplayArgument> {

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
	public void animate(EntityExampleMinotaur animated, IDisplayArgument argument,float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
		ModelElement head = getModelElement("head");
		if (head != null) {
			lookAt(head, headPitch, headYaw);
		}
	}

	@Override
	public void renderModel(PoseStack stack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
		super.renderModel(stack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha, renderCall, resetBuffer);
	}

	@Override
	public ResourceLocation getTexture(EntityExampleMinotaur minotaur) {
		return TEXTURE;
	}
}