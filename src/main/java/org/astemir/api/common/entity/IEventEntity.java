package org.astemir.api.common.entity;


import net.minecraft.world.entity.Entity;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.utils.EntityUtils;

public interface IEventEntity {

    void onHandleServerEvent(int event,PacketArgument[] arguments);

    void onHandleClientEvent(int event, PacketArgument[] arguments);

    default <T extends Entity & IEventEntity> void playClientEvent(int event, PacketArgument... arguments){
        EntityUtils.playClientEvent((T)this,event,arguments);
    }


    default <T extends Entity & IEventEntity> void playServerEvent(int event,PacketArgument... arguments){
        EntityUtils.playServerEvent((T)this,event,arguments);
    }
}
