package org.astemir.api.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.client.event.CameraAdvancedSetupEvent;
import org.astemir.api.client.event.CameraPreMatrixResetEvent;
import org.astemir.api.client.event.GlobalRenderEvent;
import org.astemir.api.math.MathUtils;
import org.astemir.example.SkillsAPI;
import org.astemir.api.client.animation.Animator;
import org.astemir.api.common.gfx.*;
import org.astemir.api.math.components.Color;
import org.astemir.api.client.render.RenderUtils;
import org.astemir.api.io.ResourceUtils;


@OnlyIn(Dist.CLIENT)
public class ClientManager {

    public static ResourceLocation BLACKOUT_TEXTURE = ResourceUtils.loadTexture(SkillsAPI.MOD_ID,"ui/blackout.png");
    

    @SubscribeEvent
    public static void onMainRender(TickEvent.RenderTickEvent e){
        if (e.phase == TickEvent.Phase.START) {
            if (Minecraft.getInstance().isPaused()) {
                return;
            }
            PlayerGFXEffectManager.getInstance().update();
            Animator.INSTANCE.removeUnusedData();
        }
    }

    @SubscribeEvent
    public static void onCameraAdvancedSetup(CameraPreMatrixResetEvent e){
        PlayerGFXEffectManager gfxEffectHandler = PlayerGFXEffectManager.getInstance();
        for (GFXEffect effect : gfxEffectHandler.getEffects()) {
            if (effect.getEffectType() == GFXEffectType.SOFT_SHAKE){
                GFXSoftShake screenShake = (GFXSoftShake) effect;
                float ticks = (float) (screenShake.getTicks() + e.getPartialTicks());
                if (screenShake.getTicks() > 0) {
                    double power = screenShake.getShakePower() * 0.1f;
                    float lerpAmount = 0.5f;
                    float x = MathUtils.lerp(0, (float) Math.sin(ticks / 4f),lerpAmount) * (float) power;
                    float y = MathUtils.lerp(0, (float) Math.sin(ticks / 2f),lerpAmount) * (float) power;
                    float z = MathUtils.lerp(0, (float) Math.sin(ticks / 3f),lerpAmount) * (float) power;
                    e.getPoseStack().translate(x, y, z);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCameraAngleSetup(ViewportEvent.ComputeCameraAngles e){
        PlayerGFXEffectManager gfxEffectHandler = PlayerGFXEffectManager.getInstance();
        for (GFXEffect effect : gfxEffectHandler.getEffects()) {
            if (effect.getEffectType() == GFXEffectType.ROUGH_SHAKE){
                GFXRoughShake screenShake = (GFXRoughShake) effect;
                float ticks = (float) (screenShake.getTicks() + e.getPartialTick());
                if (screenShake.getTicks() > 0) {
                    double power = screenShake.getShakePower();
                    float yaw = e.getYaw();
                    float pitch = e.getPitch();
                    float roll = e.getRoll();
                    float targetYaw = yaw + (float) (power * Math.sin(ticks * 2.5F + 1.0F));
                    float targetPitch = pitch + (float) (power * Math.sin(ticks * 1.5F + 2.0F));
                    float targetRoll = roll + (float) (power * Math.sin(ticks * 2.0F));
                    float lerpAmount = 0.5f;
                    e.setYaw(MathUtils.lerp(yaw, targetYaw,lerpAmount));
                    e.setPitch(MathUtils.lerp(pitch, targetPitch,lerpAmount));
                    e.setRoll(MathUtils.lerp(roll, targetRoll,lerpAmount));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGlobalRender(RenderGuiOverlayEvent.Post e){
        PlayerGFXEffectManager gfxEffectHandler = PlayerGFXEffectManager.getInstance();
        Window window = Minecraft.getInstance().getWindow();
        float width = window.getGuiScaledWidth();
        float height = window.getGuiScaledHeight();
        for (GFXEffect effect : gfxEffectHandler.getEffects()) {
            Color color = null;
            float value = 0;
            if (effect.getEffectType() == GFXEffectType.BLACK_OUT) {
                GFXBlackOut blackOut = (GFXBlackOut) effect;
                color = blackOut.getColor();
                value = blackOut.getValue().value(Minecraft.getInstance().getPartialTick());
            } else if (effect.getEffectType() == GFXEffectType.BLACK_IN) {
                GFXBlackIn blackIn = (GFXBlackIn) effect;
                color = blackIn.getColor();
                value = blackIn.getValue().value(Minecraft.getInstance().getPartialTick());
            } else if (effect.getEffectType() == GFXEffectType.BLACK_IN_OUT) {
                GFXBlackInOut blackInOut = (GFXBlackInOut) effect;
                color = blackInOut.getColor();
                value = blackInOut.getValue().value(Minecraft.getInstance().getPartialTick());
            }
            if (color != null) {
                PoseStack stack = new PoseStack();
                RenderUtils.fillColor(stack, 0, 0, (int) width, (int) height, new Color(color.r, color.g, color.b, value));
            }
        }
    }


}
