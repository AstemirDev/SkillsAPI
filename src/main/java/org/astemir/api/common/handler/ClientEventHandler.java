package org.astemir.api.common.handler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import org.astemir.api.network.PacketArgument;

public interface ClientEventHandler {

    void onHandleEvent(ClientLevel level, BlockPos pos, int event, PacketArgument[] arguments);
}
