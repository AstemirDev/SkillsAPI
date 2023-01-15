package org.astemir.api.client.wrapper;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.animation.ISARendered;


public abstract class AbstractModelWrapperItem<T extends Item & ISARendered> extends Model implements IModelWrapper<T> {

    public T renderTarget;
    public ItemStack itemStack;
    public ItemTransforms.TransformType transformType;
    public MultiBufferSource multiBufferSource;

    public AbstractModelWrapperItem() {
        super(RenderType::itemEntityTranslucentCull);
    }

    public void renderItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, VertexConsumer consumer,MultiBufferSource bufferSource, int packedLight, int packedOverlay,float r,float g,float b,float a) {
        AdvancedModel<T> model = getModel(renderTarget);
        model.modelWrapper = this;
        poseStack.pushPose();
        poseStack.translate(0, 0.01f, 0);
        poseStack.translate(0.5, 2, 0.5f);
        poseStack.scale(-1,-1,1);
        model.renderWithLayers(poseStack,consumer,packedLight,packedOverlay,r,g,b,a,RenderCall.MODEL,false);
        poseStack.popPose();
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.entityCutoutNoCull(getModel(renderTarget).getTexture(renderTarget));
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        renderItem(itemStack,transformType,poseStack,vertexConsumer,multiBufferSource,packedLight,packedOverlay,r,g,b,a);
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    @Override
    public T getRenderTarget() {
        return renderTarget;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemTransforms.TransformType getTransformType() {
        return transformType;
    }
}
