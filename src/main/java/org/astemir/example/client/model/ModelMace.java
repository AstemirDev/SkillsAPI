package org.astemir.example.client.model;


import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.item.MaceItem;


public class ModelMace extends AdvancedModel<MaceItem> {

	public static ResourceLocation MODEL = ResourceUtils.model("item/mace.geo.json");

	public ModelMace() {
		super(RenderType::entityCutoutNoCull,MODEL,null);
	}

	@Override
	public void animate(MaceItem animated, float limbSwing, float limbSwingAmount, float ticks, float delta, float headYaw, float headPitch) {

	}
}