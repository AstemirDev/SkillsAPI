package org.astemir.api.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.event.LivingTransformEvent;
import org.astemir.example.SkillsAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class},priority = 500)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Shadow
    protected abstract float getBob(T pLivingBase, float pPartialTicks);

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Inject(method = "render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V"), remap = SkillsAPI.REMAP)
    public void onSetupRotations(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        LivingTransformEvent.Rotation<T> event = new LivingTransformEvent.Rotation(pEntity,pMatrixStack,getBob(pEntity,pPartialTicks), Mth.rotLerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot),pPartialTicks);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V"), remap = SkillsAPI.REMAP)
    public void onSetupScale(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci){
        LivingTransformEvent.Scale<T> event = new LivingTransformEvent.Scale(pEntity, pMatrixStack,pPartialTicks);
        MinecraftForge.EVENT_BUS.post(event);
    }
}