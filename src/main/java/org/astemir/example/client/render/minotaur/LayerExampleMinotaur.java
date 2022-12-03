package org.astemir.example.client.render.minotaur;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.SARenderTypes;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.SkillsAPIMod;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class LayerExampleMinotaur extends RenderLayer<EntityExampleMinotaur, WrapperExampleMinotaur> {

    public final ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPIMod.MOD_ID,"entity/minotaur_eyes.png");


    public LayerExampleMinotaur(RenderLayerParent<EntityExampleMinotaur, WrapperExampleMinotaur> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(PoseStack p_117349_, MultiBufferSource p_117350_, int p_117351_, EntityExampleMinotaur p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        VertexConsumer vertexconsumer = p_117350_.getBuffer(SARenderTypes.eyesTransparent(TEXTURE));
        this.getParentModel().renderWrapper(p_117349_, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, (float) Math.abs(Math.sin(p_117352_.tickCount/20f))*0.75f+0.25f, RenderCall.LAYER,false);
    }

}
