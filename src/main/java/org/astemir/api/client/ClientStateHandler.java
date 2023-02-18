package org.astemir.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.animation.Animator;
import org.astemir.api.common.gfx.*;
import org.astemir.api.math.Color;
import org.astemir.api.client.render.RenderUtils;
import org.astemir.api.utils.ResourceUtils;


public class ClientStateHandler {

    public static ResourceLocation BLACKOUT_TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"ui/blackout.png");
    

    @SubscribeEvent
    public static void onMainRender(TickEvent.RenderTickEvent e){
        if (Minecraft.getInstance().isPaused()){
            return;
        }
        PlayerGFXEffectManager.getInstance().update();
        Animator.INSTANCE.removeUnusedData();
    }

    @SubscribeEvent
    public static void onRenderScreen(RenderGuiOverlayEvent.Pre e){
        PlayerGFXEffectManager gfxEffectHandler = PlayerGFXEffectManager.getInstance();
        if (e.getOverlay().id().equals(VanillaGuiOverlay.CROSSHAIR.id())) {
            for (GFXEffect effect : gfxEffectHandler.getEffects()) {
                Color color = null;
                float value = 0;
                if (effect.getEffectType() == GFXEffectType.BLACK_OUT) {
                    GFXBlackOut blackOut = (GFXBlackOut) effect;
                    color = blackOut.getColor();
                    value = blackOut.getValue().value(Minecraft.getInstance().getPartialTick());
                }
                if (effect.getEffectType() == GFXEffectType.BLACK_IN){
                    GFXBlackIn blackIn = (GFXBlackIn) effect;
                    color = blackIn.getColor();
                    value = blackIn.getValue().value(Minecraft.getInstance().getPartialTick());
                }
                if (effect.getEffectType() == GFXEffectType.BLACK_IN_OUT){
                    GFXBlackInOut blackInOut = (GFXBlackInOut) effect;
                    color = blackInOut.getColor();
                    value = blackInOut.getValue().value(Minecraft.getInstance().getPartialTick());
                }
                if (color != null) {
                    RenderUtils.fillScreenWithTexture(e.getPoseStack(), BLACKOUT_TEXTURE, color.r, color.g, color.b, value);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCameraRotate(ViewportEvent.ComputeCameraAngles e){
        PlayerGFXEffectManager gfxEffectHandler = PlayerGFXEffectManager.getInstance();
        for (GFXEffect effect : gfxEffectHandler.getEffects()) {
            if (effect.getEffectType() == GFXEffectType.SCREEN_SHAKE){
                GFXScreenShake screenShake = (GFXScreenShake) effect;
                double power = screenShake.getShakePower();
                int ticks = screenShake.getTicks();
                float ticksExistedDelta = (float) (ticks+e.getPartialTick());
                double shakeAmplitude = power;
                if (shakeAmplitude > 1.0F){
                    shakeAmplitude = 1.0F;
                }
                if (ticks > 0) {
                    e.setPitch((float) ((double) e.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 1.5F + 2.0F)));
                    e.setYaw((float) ((double) e.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 2.5F + 1.0F)));
                    e.setRoll((float) ((double) e.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 2.0F)));
                }
            }
        }
    }

}
