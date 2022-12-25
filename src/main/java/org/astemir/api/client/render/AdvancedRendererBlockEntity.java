package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.wrapper.AbstractModelWrapperBlockEntity;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ISARendered;

public class AdvancedRendererBlockEntity<T extends BlockEntity & ISARendered> implements BlockEntityRenderer<T> {

    private AbstractModelWrapperBlockEntity<T> blockModelWrapper;

    public AdvancedRendererBlockEntity(BlockEntityRendererProvider.Context context, AbstractModelWrapperBlockEntity wrapper) {
        this.blockModelWrapper = wrapper;
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity instanceof IAnimated){
            float ticks = ((IAnimated) blockEntity).getTicks();
            blockModelWrapper.getModel(blockEntity).setupAnim(blockEntity,0,0,ticks+partialTick,0,0);
        }
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
    }

    public AbstractModelWrapperBlockEntity<T> getBlockModelWrapper() {
        return blockModelWrapper;
    }
}
