package org.astemir.api.common.entity;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.common.entity.ai.ICustomAIEntity;

public class PlayerEventListener {


    @SubscribeEvent
    public static void onPlayerClickMob(PlayerInteractEvent.EntityInteract e){
        if (!e.getTarget().level.isClientSide) {
            if (e.getTarget() instanceof ICustomAIEntity customAIEntity) {
                customAIEntity.getAISystem().handlePlayerInteraction(e.getEntity(), e.getHand(), e.getItemStack(), e.getFace());
            }
        }
    }
}
