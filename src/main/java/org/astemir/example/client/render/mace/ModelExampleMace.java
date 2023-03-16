package org.astemir.example.client.render.mace;


import net.minecraft.resources.ResourceLocation;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.display.DisplayArgumentItem;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.item.ItemExampleMace;


public class ModelExampleMace extends SkillsModel<ItemExampleMace, DisplayArgumentItem> {

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