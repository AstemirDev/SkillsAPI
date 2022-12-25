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

    public AdvancedRendererBlockEntity(BlockEntityRendererProvider.Context p_173636_, AbstractModelWrapperBlockEntity wrapper) {
        this.blockModelWrapper = wrapper;
    }

    @Override
    public void render(T p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (p_112307_ instanceof IAnimated){
            float ticks = ((IAnimated) p_112307_).getTicks();
            blockModelWrapper.getModel(p_112307_).setupAnim(p_112307_,0,0,ticks+p_112308_,0,0);
        }
        blockModelWrapper.renderTarget = (T) p_112307_;
        blockModelWrapper.multiBufferSource = p_112310_;
        int i;
        if (p_112307_.getLevel() != null) {
            i = LevelRenderer.getLightColor(p_112307_.getLevel(), p_112307_.getBlockPos().above());
        } else {
            i = 15728880;
        }
        VertexConsumer consumer = p_112310_.getBuffer(blockModelWrapper.getRenderType());
        blockModelWrapper.renderToBuffer(p_112309_,consumer,i,p_112312_,1,1,1,1);
    }

    public AbstractModelWrapperBlockEntity<T> getBlockModelWrapper() {
        return blockModelWrapper;
    }
}
