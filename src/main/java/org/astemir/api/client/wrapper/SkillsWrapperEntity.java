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
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsModel;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.misc.ICustomRendered;


public abstract class SkillsWrapperEntity<T extends Entity & ICustomRendered> extends EntityModel<T> implements IModelWrapper<T, IDisplayArgument> {

    public T renderTarget;
    public MultiBufferSource multiBufferSource;


    public SkillsWrapperEntity() {
        super(RenderType::entityCutout);
    }

    public void renderWrapper(PoseStack poseStack, VertexConsumer bufferSource, int packedLight, int packedOverlay, float r, float g, float b, float a, RenderCall renderCall,boolean resetBuffer) {
        SkillsModel<T,IDisplayArgument> model = getModel(renderTarget);
        model.modelWrapper = this;
        model.renderToBuffer(poseStack,bufferSource,packedLight, packedOverlay, r, g, b, a);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferSource, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        renderWrapper(poseStack,bufferSource,packedLight,packedOverlay,r,g,b,calculateClientAlpha(),RenderCall.MODEL,true);
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

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {}
}
