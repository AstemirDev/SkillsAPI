package org.astemir.example.client.render.mace;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.item.ItemExampleMace;


public class ModelExampleMace extends AdvancedModel<ItemExampleMace> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPI.MOD_ID,"item/mace.geo.json");
	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"item/mace_in_hand.png");

	public ModelExampleMace() {
		super(MODEL);
	}

	@Override
	public ResourceLocation getTexture(ItemExampleMace item) {
		return TEXTURE;
	}
}