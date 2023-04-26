package org.astemir.api.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.event.GlobalRenderEvent;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.event.CameraPreMatrixResetEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GameRenderer.class},priority = 500)
public class MixinGameRenderer {

    @Inject(method = "renderLevel", remap = SkillsAPI.REMAP,at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lcom/mojang/math/Matrix4f;)V"))
    public void onMatrixReset(float pPartialTicks, long pFinishTimeNano, PoseStack pMatrixStack, CallbackInfo ci) {
        CameraPreMatrixResetEvent event = new CameraPreMatrixResetEvent(pMatrixStack,pPartialTicks,pFinishTimeNano);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "render",remap = SkillsAPI.REMAP,at = @At(value = "INVOKE",target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"))
    public void onGlobalRender(float pPartialTicks, long pNanoTime, boolean pRenderLevel, CallbackInfo ci){
        GlobalRenderEvent event = new GlobalRenderEvent(pPartialTicks, pNanoTime);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
