package org.astemir.api.client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.common.animation.ITESRModel;

public abstract class EntityModelWrapper<T extends Entity & ITESRModel> extends EntityModel<T> implements IModelWrapper<T> {

    public T renderTarget;
    public MultiBufferSource multiBufferSource;

    public EntityModelWrapper() {
        super(RenderType::entityCutout);
    }

    public void renderWrapper(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_, RenderCall renderCall,boolean resetBuffer) {
        AdvancedModel<T> model = getModel(renderTarget);
        model.modelWrapper = this;
        model.renderModel(p_103111_,p_103112_,p_103113_, p_103114_, p_103115_, p_103116_, p_103117_, p_103118_,renderCall,resetBuffer);
    }

    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        renderWrapper(p_103111_,p_103112_,p_103113_,p_103114_,p_103115_,p_103116_,p_103117_,calculateClientAlpha(),RenderCall.MODEL,true);
    }

    @Override
    public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
        getModel(renderTarget).setupAnim(p_102618_,p_102619_,p_102620_,p_102621_,p_102622_,p_102623_);
    }

    public RenderType getRenderType(T entity, ResourceLocation texture){
        if (hasInvisibility() || entity.isInvisible()){
            return RenderType.entityTranslucentCull(texture);
        }
        return RenderType.entityCutoutNoCull(texture);
    }

    public float calculateClientAlpha(){
        if (getRenderTarget().isInvisible() || hasInvisibility()){
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isSpectator()){
                return 0.25f;
            }else{
                return 0;
            }
        }
        return 1;
    }

    private boolean hasInvisibility(){
        if (getRenderTarget() instanceof LivingEntity){
            if (((LivingEntity)getRenderTarget()).hasEffect(MobEffects.INVISIBILITY)){
                return true;
            }
        }
        return false;
    }

    public abstract AdvancedModel<T> getModel(T target);

    public abstract ResourceLocation getTexture(T target);

    public T getRenderTarget() {
        return renderTarget;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }
}
