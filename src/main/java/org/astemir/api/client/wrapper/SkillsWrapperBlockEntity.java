package org.astemir.api.client.wrapper;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.misc.ICustomRendered;


public abstract class SkillsWrapperBlockEntity<T extends BlockEntity & ICustomRendered> extends Model implements IModelWrapper<T, IDisplayArgument> {

    public T renderTarget;
    public MultiBufferSource multiBufferSource;



    public SkillsWrapperBlockEntity() {
        super(RenderType::entityCutoutNoCull);
    }

    public void renderBlock(PoseStack poseStack, VertexConsumer consumer,MultiBufferSource bufferSource, int packedLight, int packedOverlay,float r,float g,float b,float a) {
        float partialTicks = Minecraft.getInstance().getPartialTick();
        SkillsModel<T,IDisplayArgument> model = getModel(renderTarget);
        model.modelWrapper = this;
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5f, 0.5f);
        scale(getRenderTarget(),poseStack, partialTicks);
        setupRotations(getRenderTarget(),poseStack, partialTicks);
        model.renderToBuffer(poseStack,consumer,packedLight,packedOverlay,r,g,b,a);
        poseStack.popPose();
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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferSource, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        renderBlock(poseStack,bufferSource,multiBufferSource,packedLight,packedOverlay,r,g,b,a);
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    @Override
    public T getRenderTarget() {
        return renderTarget;
    }
}
