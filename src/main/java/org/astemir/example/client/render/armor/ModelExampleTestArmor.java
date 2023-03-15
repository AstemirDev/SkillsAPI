package org.astemir.example.client.render.armor;


import net.minecraft.resources.ResourceLocation;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.display.DisplayArgumentArmor;
import org.astemir.api.client.model.SunModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.item.armor.ArmorItemExample;


public class ModelExampleTestArmor extends SunModel<ArmorItemExample, DisplayArgumentArmor> {

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