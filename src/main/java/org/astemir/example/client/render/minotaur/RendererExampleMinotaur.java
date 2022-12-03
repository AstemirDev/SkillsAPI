package org.astemir.example.client.render.minotaur;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.astemir.api.client.render.AdvancedRendererLivingEntity;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class RendererExampleMinotaur extends AdvancedRendererLivingEntity<EntityExampleMinotaur, WrapperExampleMinotaur> {

    public RendererExampleMinotaur(EntityRendererProvider.Context context) {
        super(context,new WrapperExampleMinotaur());
        addLayer(new LayerExampleMinotaur(this));
    }

    @Override
    protected void setupRotations(EntityExampleMinotaur p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        if (p_115317_.moveController.is(EntityExampleMinotaur.ACTION_INFINITE)){
            p_115318_.mulPose(Vector3f.XN.rotationDegrees(90));
        }
        super.setupRotations(p_115317_, p_115318_, p_115319_, p_115320_, p_115321_);
    }
}
