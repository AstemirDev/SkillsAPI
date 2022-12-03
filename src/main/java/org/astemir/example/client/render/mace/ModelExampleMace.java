package org.astemir.example.client.render.mace;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.item.MaceItem;


public class ModelExampleMace extends AdvancedModel<MaceItem> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"item/mace.geo.json");
	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPIMod.MOD_ID,"item/mace_in_hand.png");

	public ModelExampleMace() {
		super(MODEL);
	}

	@Override
	public ResourceLocation getTexture(MaceItem item) {
		return TEXTURE;
	}
}