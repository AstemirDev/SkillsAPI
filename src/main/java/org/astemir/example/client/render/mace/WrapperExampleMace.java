package org.astemir.example.client.render.mace;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.AbstractModelWrapperItem;
import org.astemir.example.common.item.ItemExampleMace;

public class WrapperExampleMace extends AbstractModelWrapperItem<ItemExampleMace> {

    private final ModelExampleMace MODEL = new ModelExampleMace();

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }

    @Override
    public AdvancedModel<ItemExampleMace> getModel(ItemExampleMace item) {
        return MODEL;
    }
}
