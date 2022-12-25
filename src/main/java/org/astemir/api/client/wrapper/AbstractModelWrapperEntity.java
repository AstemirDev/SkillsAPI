package org.astemir.api.client.wrapper;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.animation.ISARendered;

public abstract class AbstractModelWrapperEntity<T extends Entity & ISARendered> extends EntityModel<T> implements IModelWrapper<T> {

    public T renderTarget;
    public MultiBufferSource multiBufferSource;

    public AbstractModelWrapperEntity() {
        super(RenderType::entityCutout);
    }

    public void renderWrapper(PoseStack poseStack, VertexConsumer bufferSource, int packedLight, int packedOverlay, float r, float g, float b, float a, RenderCall renderCall,boolean resetBuffer) {
        AdvancedModel<T> model = getModel(renderTarget);
        model.modelWrapper = this;
        model.renderModel(poseStack,bufferSource,packedLight, packedOverlay, r, g, b, a,renderCall,resetBuffer);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferSource, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        renderWrapper(poseStack,bufferSource,packedLight,packedOverlay,r,g,b,calculateClientAlpha(),RenderCall.MODEL,true);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        getModel(entity).setupAnim(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
    }

    public RenderType getDefaultRenderType(){
        return RenderType.entityCutoutNoCull(getModel(getRenderTarget()).getTexture(getRenderTarget()));
    }

    @Override
    public RenderType getRenderType() {
        if (hasInvisibility() || renderTarget.isInvisible()){
            return RenderType.entityTranslucent(getModel(renderTarget).getTexture(renderTarget));
        }
        return getDefaultRenderType();
    }

    public float calculateClientAlpha(){
        if (renderTarget.isInvisible() || hasInvisibility()){
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isSpectator()){
                return 0.15f;
            }else{
                return 0;
            }
        }
        return 1;
    }

    private boolean hasInvisibility(){
        if (renderTarget instanceof LivingEntity){
            if (((LivingEntity)getRenderTarget()).hasEffect(MobEffects.INVISIBILITY)){
                return true;
            }
        }
        return false;
    }


    public T getRenderTarget() {
        return renderTarget;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }
}
