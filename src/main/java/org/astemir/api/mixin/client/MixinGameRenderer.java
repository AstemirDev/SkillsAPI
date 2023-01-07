package org.astemir.api.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.astemir.api.client.event.CameraPreSetupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameRenderer.class})
public class MixinGameRenderer {

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lcom/mojang/math/Matrix4f;)V"))
    public void renderLevel(float pPartialTicks, long pFinishTimeNano, PoseStack pMatrixStack, CallbackInfo ci) {
        CameraPreSetupEvent event = new CameraPreSetupEvent(pMatrixStack,pPartialTicks,pFinishTimeNano);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
