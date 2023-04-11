package org.astemir.api.common.entity;


import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.astemir.api.common.entity.utils.EntityUtils;
import org.astemir.api.common.handler.CustomEvent;
import org.astemir.api.common.handler.CustomEventMap;
import org.astemir.api.network.PacketArgument;

public interface IEventEntity {


    default void onHandleServerEvent(int event,PacketArgument[] arguments){};

    @OnlyIn(Dist.CLIENT)
    default void onHandleClientEvent(int event, PacketArgument[] arguments){};

    default <T extends Entity & IEventEntity> void playClientEvent(int event, PacketArgument... arguments){
        EntityUtils.playClientEvent((T)this,event,arguments);
    }

    default <T extends Entity & IEventEntity> void playServerEvent(int event,PacketArgument... arguments){
        EntityUtils.playServerEvent((T)this,event,arguments);
    }

    default <T extends Entity & IEventEntity> void playClientEvent(CustomEvent event,PacketArgument... arguments){
        if (event.getValue() != null) {
            EntityUtils.playClientEvent((T) this, event.getValue(), arguments);
        }
    }

    default <T extends Entity & IEventEntity> void playServerEvent(CustomEvent event,PacketArgument... arguments){
        if (event.getValue() != null) {
            EntityUtils.playServerEvent((T) this, event.getValue(), arguments);
        }
    }

    default CustomEventMap clientEventMap(){
        return null;
    }

    default CustomEventMap serverEventMap(){
        return null;
    }
}
