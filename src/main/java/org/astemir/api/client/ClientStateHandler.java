package org.astemir.api.client;

import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.client.animation.AnimationDataHandler;
import org.astemir.api.SkillsAPI;
import org.astemir.api.common.PlayerScreenShaker;

public class ClientStateHandler {

    public static AnimationDataHandler ANIMATION_DATA_HANDLER = new AnimationDataHandler();

    @SubscribeEvent
    public static void onMainRender(TickEvent.RenderTickEvent e){
        ANIMATION_DATA_HANDLER.removeUnusedData();
    }


    @SubscribeEvent
    public static void onCameraRotate(ViewportEvent.ComputeCameraAngles e){
        PlayerScreenShaker shaker = SkillsAPI.API.SCREEN_SHAKER;
        float power = shaker.getShakePower();
        int ticks = shaker.getShakeTicks();
        float ticksExistedDelta = (float) (ticks+e.getPartialTick());
        float shakeAmplitude = power;
        if (shakeAmplitude > 1.0F){
            shakeAmplitude = 1.0F;
        }
        if (ticks > 0) {
            e.setPitch((float) ((double) e.getPitch() + (double) shakeAmplitude * Math.cos(ticksExistedDelta * 1.5F + 2.0F)));
            e.setYaw((float) ((double) e.getYaw() + (double) shakeAmplitude * Math.cos(ticksExistedDelta * 2.5F + 1.0F)));
            e.setRoll((float) ((double) e.getRoll() + (double) shakeAmplitude * Math.cos(ticksExistedDelta * 2.0F)));
        }
        shaker.update();
    }

}
