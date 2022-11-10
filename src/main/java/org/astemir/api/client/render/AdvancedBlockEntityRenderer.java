package org.astemir.api.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.model.BlockEntityModelWrapper;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ITESRModel;

public class AdvancedBlockEntityRenderer<T extends BlockEntity & ITESRModel> implements BlockEntityRenderer<T> {

    private BlockEntityModelWrapper blockModelWrapper;

    public AdvancedBlockEntityRenderer(BlockEntityRendererProvider.Context p_173636_,BlockEntityModelWrapper wrapper) {
        this.blockModelWrapper = wrapper;
    }

    @Override
    public void render(BlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (p_112307_ instanceof ITESRModel){
            ITESRModel target = (ITESRModel) p_112307_;
            if (target instanceof IAnimated){
                float ticks = ((IAnimated) target).getTicks();
                blockModelWrapper.getModel(target).setupAnim(target,0,0,ticks+p_112308_,0,0);
            }
            blockModelWrapper.renderTarget = p_112307_;
            blockModelWrapper.multiBufferSource = p_112310_;
            int i;
            if (p_112307_.getLevel() != null) {
                i = LevelRenderer.getLightColor(p_112307_.getLevel(), p_112307_.getBlockPos().above());
            } else {
                i = 15728880;
            }
            VertexConsumer consumer = p_112310_.getBuffer(blockModelWrapper.getRenderType((BlockEntity) target,blockModelWrapper.getTexture(target)));
            blockModelWrapper.renderBlock((BlockEntity) target,p_112309_,consumer,p_112310_,i,p_112312_,1,1,1,1);
        }
    }

    public BlockEntityModelWrapper getBlockModelWrapper() {
        return blockModelWrapper;
    }
}
