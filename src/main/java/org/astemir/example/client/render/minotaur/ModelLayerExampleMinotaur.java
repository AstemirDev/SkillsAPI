package org.astemir.example.client.render.minotaur;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.SkillsAPI;
import org.astemir.api.client.SARenderTypes;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.client.model.ModelRenderLayer;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class ModelLayerExampleMinotaur extends ModelRenderLayer<EntityExampleMinotaur,ModelExampleMinotaur> {

    public final ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"entity/minotaur_eyes.png");

    public ModelLayerExampleMinotaur(ModelExampleMinotaur model) {
        super(model);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, EntityExampleMinotaur instance, int pPackedLight, float pPartialTick, float r, float g, float b, float a) {
        //getModel().renderModel(pPoseStack,pBuffer.getBuffer(getBuffer(instance)),getEyeLight(), OverlayTexture.NO_OVERLAY,r,g,b,a, RenderCall.LAYER,false);
    }

    @Override
    public ResourceLocation getTexture(EntityExampleMinotaur instance) {
        return TEXTURE;
    }

    public RenderType getBuffer(EntityExampleMinotaur entity){
        return SARenderTypes.eyesTransparent(getTexture(entity));
    }

    public int getEyeLight(){
        return 15728640;
    }
}
