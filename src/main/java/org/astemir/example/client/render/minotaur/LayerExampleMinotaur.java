package org.astemir.example.client.render.minotaur;


import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.render.AdvancedLayerRenderer;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class LayerExampleMinotaur extends AdvancedLayerRenderer<EntityExampleMinotaur, WrapperExampleMinotaur> {

    public final ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"entity/minotaur_eyes.png");


    public LayerExampleMinotaur(RenderLayerParent<EntityExampleMinotaur, WrapperExampleMinotaur> p_117346_) {
        super(p_117346_);
    }

    @Override
    protected ResourceLocation getTextureLocation(EntityExampleMinotaur p_117348_) {
        return TEXTURE;
    }
}
