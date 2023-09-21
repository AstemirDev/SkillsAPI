package org.astemir.api.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.astemir.api.network.PacketArgument;

public interface ServerEventHandler {
    void onHandleEvent(ServerLevel level, BlockPos pos, int event, PacketArgument[] arguments);
}
