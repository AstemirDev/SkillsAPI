package org.astemir.example.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import org.astemir.api.client.wrapper.EntityModelWrapper;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.example.client.model.ModelMinotaur;
import org.astemir.example.common.entity.EntityMinotaur;

public class ModelWrapperMinotaur extends EntityModelWrapper<EntityMinotaur> {

    private final ModelMinotaur MODEL = new ModelMinotaur();


    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        super.renderToBuffer(p_103111_, p_103112_, p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_);
    }


    @Override
    public AdvancedModel<EntityMinotaur> getModel(EntityMinotaur minotaur) {
        return MODEL;
    }
}
