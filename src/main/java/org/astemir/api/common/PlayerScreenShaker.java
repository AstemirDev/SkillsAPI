package org.astemir.api.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.network.messages.ScreenShakeMessage;
import org.astemir.example.SkillsAPIMod;

public class PlayerScreenShaker {

    private int shakeTicks = 0;
    private float shakePower = 0;

    public static void shakeScreen(ServerPlayer player, int ticks, float power){
        SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.PLAYER.with(()->player), new ScreenShakeMessage(power,ticks));
    }

    @Deprecated
    public void shake(int ticks,float power){
        shakeTicks = ticks;
        shakePower = power;
    }

    public void update(){
        if (shakeTicks > 0){
            shakeTicks--;
        }
    }

    public int getShakeTicks() {
        return shakeTicks;
    }

    public float getShakePower() {
        return shakePower;
    }
}
