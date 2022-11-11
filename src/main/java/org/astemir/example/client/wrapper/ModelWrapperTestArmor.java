package org.astemir.example.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.model.ArmorModelWrapper;
import org.astemir.api.client.model.ItemModelWrapper;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.ExampleAPIMod;
import org.astemir.example.client.model.ModelMace;
import org.astemir.example.client.model.ModelTestArmor;
import org.astemir.example.common.item.MaceItem;
import org.astemir.example.common.item.armor.TestArmor;

public class ModelWrapperTestArmor extends ArmorModelWrapper<TestArmor> {


    public static ResourceLocation TEXTURE = ResourceUtils.texture(ExampleAPIMod.MOD_ID,"armor/test_armor.png");
    private final ModelTestArmor MODEL = new ModelTestArmor();

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }

    @Override
    public AdvancedModel<TestArmor> getModel(TestArmor target) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTexture(TestArmor target) {
        return TEXTURE;
    }
}
