package org.astemir.api.common.entity;


import org.astemir.api.network.PacketArgument;

public interface IEventEntity {

    void onHandleServerEvent(int event,PacketArgument[] arguments);

    void onHandleClientEvent(int event, PacketArgument[] arguments);
}
