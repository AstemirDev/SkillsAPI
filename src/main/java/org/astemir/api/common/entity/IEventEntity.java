package org.astemir.api.common.entity;


import net.minecraft.world.entity.Entity;
import org.astemir.api.common.entity.utils.EntityUtils;
import org.astemir.api.network.PacketArgument;

public interface IEventEntity {

    default void onHandleServerEvent(int event,PacketArgument[] arguments){};

    default void onHandleClientEvent(int event, PacketArgument[] arguments){};

    default <T extends Entity & IEventEntity> void playClientEvent(int event, PacketArgument... arguments){
        EntityUtils.playClientEvent((T)this,event,arguments);
    }

    default <T extends Entity & IEventEntity> void playServerEvent(int event,PacketArgument... arguments){
        EntityUtils.playServerEvent((T)this,event,arguments);
    }
}
