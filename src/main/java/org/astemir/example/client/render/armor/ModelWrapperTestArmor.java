package org.astemir.example.client.render.armor;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.AbstractModelWrapperArmor;
import org.astemir.example.common.item.armor.TestArmor;

public class ModelWrapperTestArmor extends AbstractModelWrapperArmor<TestArmor> {

    private final ModelExampleTestArmor MODEL = new ModelExampleTestArmor();

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }

    @Override
    public AdvancedModel<TestArmor> getModel(TestArmor armor) {
        return MODEL;
    }
}
