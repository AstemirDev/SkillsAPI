package org.astemir.api.client.wrapper;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.animation.ISARendered;


public abstract class AbstractModelWrapperBlockEntity<T extends BlockEntity & ISARendered> extends Model implements IModelWrapper<T> {

    public T renderTarget;
    public MultiBufferSource multiBufferSource;


    public AbstractModelWrapperBlockEntity() {
        super(RenderType::entityCutoutNoCull);
    }

    public void renderBlock(PoseStack p_103111_, VertexConsumer consumer,MultiBufferSource bufferSource, int p_103113_, int p_103114_,float r,float g,float b,float a) {
        AdvancedModel<T> model = getModel(renderTarget);
        model.modelWrapper = this;
        PoseStack stack = p_103111_;
        stack.pushPose();
        stack.translate(0.5, 1.5f, 0.5f);
        scale(getRenderTarget(),stack, Minecraft.getInstance().getPartialTick());
        setupRotations(getRenderTarget(),stack, Minecraft.getInstance().getPartialTick());
        model.renderModel(p_103111_,consumer,p_103113_,p_103114_,r,g,b,a,RenderCall.MODEL,false);
        stack.popPose();
    }


    protected void setupRotations(T blockEntity, PoseStack stack, float partialTicks) {
    }

    protected void scale(T entity,PoseStack stack, float partialTicks){
        stack.scale(-1,-1,1);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.entityCutoutNoCull(getModel(renderTarget).getTexture(renderTarget));
    }

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        renderBlock(p_103111_,p_103112_,multiBufferSource,p_103113_,p_103114_,p_103115_,p_103116_,p_103117_,p_103118_);
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    @Override
    public T getRenderTarget() {
        return renderTarget;
    }
}
