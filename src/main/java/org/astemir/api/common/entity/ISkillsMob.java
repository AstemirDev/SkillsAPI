package org.astemir.api.common.entity;

import org.astemir.api.common.action.IActionListener;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.misc.ICustomRendered;
import org.astemir.api.network.PacketArgument;

public interface ISkillsMob extends IEventEntity, IActionListener, ICustomRendered, IAnimatedEntity {

    @Override
    default void onHandleServerEvent(int event, PacketArgument[] arguments) {}

    @Override
    default void onHandleClientEvent(int event, PacketArgument[] arguments) {}
}
