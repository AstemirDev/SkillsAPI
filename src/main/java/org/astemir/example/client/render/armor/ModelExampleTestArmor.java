package org.astemir.example.client.render.armor;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.item.armor.TestArmor;


public class ModelExampleTestArmor extends AdvancedModel<TestArmor> {

	public static ResourceLocation MODEL = ResourceUtils.loadModel(SkillsAPIMod.MOD_ID,"item/armor.geo.json");
	public static ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPIMod.MOD_ID,"armor/test_armor.png");

	public ModelExampleTestArmor() {
		super(MODEL);
	}

	@Override
	public ResourceLocation getTexture(TestArmor armor) {
		return TEXTURE;
	}
}