package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.wrapper.IModelWrapper;
import org.astemir.api.client.wrapper.SkillsWrapperBlockEntity;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.misc.ICustomRendered;

public class SkillsRendererBlockEntity<T extends BlockEntity & ICustomRendered> implements BlockEntityRenderer<T>,ISkillsRenderer<T, IDisplayArgument> {

    private SkillsWrapperBlockEntity<T> blockModelWrapper;


    public SkillsRendererBlockEntity(BlockEntityRendererProvider.Context context, SkillsWrapperBlockEntity wrapper) {
        this.blockModelWrapper = wrapper;
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        blockModelWrapper.renderTarget = (T) blockEntity;
        blockModelWrapper.multiBufferSource = bufferSource;
        int light = 0;
        if (blockEntity.getLevel() != null) {
            light = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
        } else {
            light = 15728880;
        }
        VertexConsumer consumer = bufferSource.getBuffer(blockModelWrapper.getRenderType());
        blockModelWrapper.renderToBuffer(poseStack,consumer,light,packedOverlay,1,1,1,1);
        animate(blockEntity,partialTick);
    }


    @Override
    public void animate(T instance, float partialTicks) {
        if (instance instanceof IAnimatedBlock animatedBlock){
            blockModelWrapper.getModel(instance).setupAnim(instance,null,0,0,getTicks((int) animatedBlock.getTicks()),0,0);
        }
    }

    @Override
    public IModelWrapper<T, IDisplayArgument> getModelWrapper() {
        return blockModelWrapper;
    }
}
