package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.astemir.api.client.SARenderTypes;
import org.astemir.api.client.wrapper.AbstractModelWrapperEntity;
import org.astemir.api.common.animation.ITESRModel;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.client.render.minotaur.WrapperExampleMinotaur;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class AdvancedLayerRenderer<T extends Entity & ITESRModel, M extends AbstractModelWrapperEntity<T>> extends RenderLayer<T, M> {

    public AdvancedLayerRenderer(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, T p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        renderWrapper(p_117349_, p_117350_, p_117351_, p_117352_, p_117353_, p_117354_, p_117355_, p_117356_, p_117357_, p_117358_);
    }

    public void renderWrapper(PoseStack p_117349_,MultiBufferSource source,int p_117351_, T p_117352_, float p_117353_, float p_117354_, float r, float g, float b, float a){
        VertexConsumer vertexconsumer = source.getBuffer(getBuffer(p_117352_));
        this.getParentModel().renderWrapper(p_117349_, vertexconsumer, getEyeLight(), OverlayTexture.NO_OVERLAY, r, g, b, a, RenderCall.LAYER,false);
    }

    public RenderType getBuffer(T entity){
        return SARenderTypes.eyesTransparent(getTextureLocation(entity));
    }


    public int getEyeLight(){
        return 15728640;
    }
}
