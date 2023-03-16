package org.astemir.example.client.render.minotaur;


import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.math.MathUtils;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.SkillsRenderTypes;
import org.astemir.api.client.model.SkillsModelLayer;
import org.astemir.api.utils.ResourceUtils;
import org.astemir.example.common.entity.EntityExampleMinotaur;

public class ModelLayerExampleMinotaur extends SkillsModelLayer<EntityExampleMinotaur, IDisplayArgument,ModelExampleMinotaur> {

    public final ResourceLocation TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"entity/minotaur_eyes.png");

    public ModelLayerExampleMinotaur(ModelExampleMinotaur model) {
        super(model);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, EntityExampleMinotaur instance, int pPackedLight, float pPartialTick, float r, float g, float b, float a) {
        float ticks = ((float)(instance.tickCount))+pPartialTick;
        float f = MathUtils.abs(MathUtils.sin(ticks/20f));
        renderModel(pPoseStack,pBuffer,getEyeLight(),instance,r,g,b,MathUtils.clamp(f-0.5f,0,1));
        renderModel(pPoseStack,pBuffer,SkillsRenderTypes.eyesTransparentBlurry(getTexture(instance)),getEyeLight(),r,g,b,f);
    }

    @Override
    public ResourceLocation getTexture(EntityExampleMinotaur instance) {
        return TEXTURE;
    }

    @Override
    public RenderType getRenderType(EntityExampleMinotaur instance) {
        return SkillsRenderTypes.eyes(getTexture(instance));
    }

    public int getEyeLight(){
        return 15728640;
    }
}
