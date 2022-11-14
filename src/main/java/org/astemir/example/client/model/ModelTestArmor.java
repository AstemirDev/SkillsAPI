package org.astemir.example.client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.render.AdvancedCubeRenderer;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.ExampleAPIMod;
import org.astemir.example.common.item.MaceItem;
import org.astemir.example.common.item.armor.TestArmor;


public class ModelTestArmor extends AdvancedModel<TestArmor> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(ExampleAPIMod.MOD_ID,"item/armor.geo.json");

	public ModelTestArmor() {
		super(RenderType::entityCutoutNoCull,MODEL,null);
	}

	@Override
	public void onRenderModelCube(AdvancedCubeRenderer cube, PoseStack matrixStackIn, VertexConsumer bufferIn, RenderCall renderCall, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	}

	@Override
	public void animate(TestArmor animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {
	}
}