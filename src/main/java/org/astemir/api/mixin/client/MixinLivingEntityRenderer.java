package org.astemir.api.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.event.LivingAdvancedRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class},priority = 500)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Inject(method = "setupRotations",at = @At("TAIL"), remap = SkillsAPI.REMAP)
    public void setupRotations(T pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks, CallbackInfo ci) {
        LivingAdvancedRenderEvent.Rotation<T> event = new LivingAdvancedRenderEvent.Rotation(pEntityLiving,pMatrixStack,pAgeInTicks,pRotationYaw,pPartialTicks);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "scale",at=@At("TAIL"), remap = SkillsAPI.REMAP)
    public void setupScale(T pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime, CallbackInfo ci){
        LivingAdvancedRenderEvent.Scale<T> event = new LivingAdvancedRenderEvent.Scale(pLivingEntity, pMatrixStack,pPartialTickTime);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
