package org.astemir.api.common.entity;


import org.astemir.api.network.PacketArgument;

public interface IEventEntity {

    void onHandleClientEvent(int event, PacketArgument[] arguments);
}
