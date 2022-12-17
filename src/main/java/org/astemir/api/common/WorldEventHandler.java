package org.astemir.api.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.astemir.api.network.PacketArgument;

import java.util.ArrayList;
import java.util.List;

public class WorldEventHandler {

    public static WorldEventHandler INSTANCE = new WorldEventHandler();

    private List<IClientWorldEventListener> clientListeners = new ArrayList<>();
    private List<IServerWorldEventListener> serverListeners = new ArrayList<>();



    public void addClientListener(IClientWorldEventListener listener){
        clientListeners.add(listener);
    }

    public void addServerListener(IServerWorldEventListener listener){
        serverListeners.add(listener);
    }


    public void onClientHandleEvent(BlockPos pos, int event, PacketArgument[] arguments){
        ClientLevel level = Minecraft.getInstance().level;
        for (IClientWorldEventListener listener : clientListeners) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }


    public void onServerHandleEvent(ServerLevel level,BlockPos pos, int event, PacketArgument[] arguments){
        for (IServerWorldEventListener listener : serverListeners) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }

    public interface IClientWorldEventListener {
        void onHandleEvent(ClientLevel level,BlockPos pos, int event, PacketArgument[] arguments);
    }

    public interface IServerWorldEventListener{
        void onHandleEvent(ServerLevel level, BlockPos pos, int event, PacketArgument[] arguments);
    }

    public static WorldEventHandler getInstance(){
        return INSTANCE;
    }

}
