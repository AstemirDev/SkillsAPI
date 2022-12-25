package org.astemir.example.client.render.armor;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.item.armor.ArmorItemExample;


public class ModelExampleTestArmor extends AdvancedModel<ArmorItemExample> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPI.MOD_ID,"item/armor.geo.json");
	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"armor/test_armor.png");

	public ModelExampleTestArmor() {
		super(MODEL);
	}

	@Override
	public ResourceLocation getTexture(ArmorItemExample armor) {
		return TEXTURE;
	}
}