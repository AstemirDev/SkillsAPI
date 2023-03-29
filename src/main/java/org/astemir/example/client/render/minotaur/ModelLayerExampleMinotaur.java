package org.astemir.example.client.render.minotaur;


import com.lowdragmc.shimmer.client.ShimmerRenderTypes;
import com.lowdragmc.shimmer.client.postprocessing.PostMultiBufferSource;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.lowdragmc.shimmer.core.mixins.ShimmerMixinPlugin;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.astemir.api.client.SkillsRenderTypes;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.example.SkillsAPI;
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
        float ticks = ((float)(instance.tickCount))+ Minecraft.getInstance().getPartialTick();
        PoseStack finalStack = RenderUtils.copyPoseStack(pPoseStack);
        PostProcessing.BLOOM_UNREAL.postEntityForce(sourceConsumer ->{
            renderModel(finalStack,sourceConsumer,getEyeLight(),instance,1,1,1,1);
        });
    }


    @Override
    public ResourceLocation getTexture(EntityExampleMinotaur instance) {
        return TEXTURE;
    }

    @Override
    public RenderType getRenderType(EntityExampleMinotaur instance) {
        return SkillsRenderTypes.testBloom(getTexture(instance));
    }

    public int getEyeLight(){
        return 15728640;
    }
}
