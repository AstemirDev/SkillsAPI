package org.astemir.example.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.wrapper.ItemModelWrapper;
import org.astemir.example.client.model.ModelMace;
import org.astemir.example.common.item.MaceItem;

public class ModelWrapperMace extends ItemModelWrapper<MaceItem> {

    private final ModelMace MODEL = new ModelMace();

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }

    @Override
    public AdvancedModel<MaceItem> getModel() {
        return MODEL;
    }
}
