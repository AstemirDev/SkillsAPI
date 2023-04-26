package org.astemir.api.lib.shimmer;


import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleOptions;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.api.lib.ModLib;

import java.util.function.Consumer;


public class ShimmerLib extends ModLib {

    public static final ShimmerLib INSTANCE = new ShimmerLib();

    public static int LIGHT_UNSHADED = 15728640;

    public ShimmerLib() {
        super("shimmer");
    }

    public static boolean isLoaded(){
        return INSTANCE.isEnabled();
    }

    public static void postModelForce(PoseStack stack, SkillsModel model, RenderType renderType, int packedLight, int packedOverlay, float r, float g, float b, float a){
        if (isLoaded()) {
            PoseStack copyStack = RenderUtils.copyPoseStack(stack);
            PostProcessing.BLOOM_UNREAL.postEntityForce((source) -> {
                VertexConsumer consumer = source.getBuffer(renderType);
                model.renderModel(copyStack, consumer, packedLight, packedOverlay, r, g, b, a, RenderCall.LAYER, false);
            });
        }
    }

    public static void postModelWrapperForce(PoseStack stack, IModelWrapper wrapper, RenderType renderType, ICustomRendered entity, int packedLight, int packedOverlay, float r, float g, float b, float a){
        if (isLoaded()) {
            PoseStack copyStack = RenderUtils.copyPoseStack(stack);
            PostProcessing.BLOOM_UNREAL.postEntityForce((source) -> {
                VertexConsumer consumer = source.getBuffer(renderType);
                wrapper.getModel(entity).renderModel(copyStack, consumer, packedLight, packedOverlay, r, g, b, a, RenderCall.LAYER, false);
            });
        }
    }


    public static void postModelForce(PoseStack stack, SkillsModel model, RenderType renderType, int packedLight, int packedOverlay, float r, float g, float b, float a, Consumer<MultiBufferSource> run){
        if (isLoaded()) {
            PoseStack copyStack = RenderUtils.copyPoseStack(stack);
            PostProcessing.BLOOM_UNREAL.postEntityForce((source) -> {
                run.accept(source);
                VertexConsumer consumer = source.getBuffer(renderType);
                model.renderModel(copyStack, consumer, packedLight, packedOverlay, r, g, b, a, RenderCall.LAYER, false);
            });
        }
    }

    public static void postModelWrapperForce(PoseStack stack, IModelWrapper wrapper, RenderType renderType, ICustomRendered entity, int packedLight, int packedOverlay, float r, float g, float b, float a,Consumer<MultiBufferSource> run){
        if (isLoaded()) {
            PoseStack copyStack = RenderUtils.copyPoseStack(stack);
            PostProcessing.BLOOM_UNREAL.postEntityForce((source) -> {
                run.accept(source);
                VertexConsumer consumer = source.getBuffer(renderType);
                wrapper.getModel(entity).renderModel(copyStack, consumer, packedLight, packedOverlay, r, g, b, a, RenderCall.LAYER, false);
            });
        }
    }

    public static void postProcess(PostProcessing processing, Consumer<MultiBufferSource> run){
        if (isLoaded()) {
            processing.postEntityForce(run);
        }
    }

    public static void spawnParticle(Particle particle){
        if (isLoaded()) {
            PostProcessing.BLOOM_UNREAL.postParticle(particle);
        }
    }

    public static void spawnParticle(ParticleOptions options, float x, float y, float z, float speedX, float speedY, float speedZ){
        if (isLoaded()) {
            PostProcessing.BLOOM_UNREAL.postParticle(options, x, y, z, speedX, speedY, speedZ);
        }
    }

    public static void renderEntityPost(){
        if (isLoaded()) {
            PostProcessing.BLOOM_UNREAL.renderEntityPost(Minecraft.getInstance().getProfiler());
        }
    }

    public static void renderEntityPost(PostProcessing processing){
        if (isLoaded()) {
            processing.renderEntityPost(Minecraft.getInstance().getProfiler());
        }
    }

}
