package org.astemir.example.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.astemir.api.client.render.AdvancedLivingRenderer;
import org.astemir.example.client.wrapper.ModelWrapperMinotaur;
import org.astemir.example.client.layer.ModelLayerMinotaur;
import org.astemir.example.common.entity.EntityMinotaur;

public class MinotaurRenderer extends AdvancedLivingRenderer<EntityMinotaur, ModelWrapperMinotaur> {

    public MinotaurRenderer(EntityRendererProvider.Context context) {
        super(context,new ModelWrapperMinotaur());
        addLayer(new ModelLayerMinotaur(this));
    }
}
