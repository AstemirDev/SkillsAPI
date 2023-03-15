package org.astemir.example.client.render.minotaur;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.wrapper.MWEntity;
import org.astemir.api.client.model.SunModel;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class WrapperExampleMinotaur extends MWEntity<EntityExampleMinotaur> {

    private final ModelExampleMinotaur MODEL = new ModelExampleMinotaur();


    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }


    @Override
    public SunModel<EntityExampleMinotaur, IDisplayArgument> getModel(EntityExampleMinotaur minotaur) {
        return MODEL;
    }
}
