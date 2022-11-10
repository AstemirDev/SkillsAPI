package org.astemir.example.client.wrapper;


import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.model.EntityModelWrapper;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.client.model.ModelMinotaur;
import org.astemir.example.common.entity.EntityMinotaur;

public class ModelWrapperMinotaur extends EntityModelWrapper<EntityMinotaur> {

    public final ResourceLocation TEXTURE = ResourceUtils.texture("entity/minotaur.png");

    private final ModelMinotaur MODEL = new ModelMinotaur();


    @Override
    public AdvancedModel<EntityMinotaur> getModel(EntityMinotaur target) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTexture(EntityMinotaur target) {
        return TEXTURE;
    }
}
