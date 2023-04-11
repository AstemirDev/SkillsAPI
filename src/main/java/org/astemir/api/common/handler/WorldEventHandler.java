package org.astemir.api.common.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.network.messages.ClientMessageWorldPosEvent;
import org.astemir.api.network.messages.ServerMessageWorldPosEvent;
import org.astemir.api.network.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class WorldEventHandler {

    public static WorldEventHandler INSTANCE = new WorldEventHandler();

    private Map<String,IClientWorldEventListener> clientListeners = new HashMap<>();
    private Map<String,IServerWorldEventListener> serverListeners = new HashMap<>();

    public void addClientListener(String name,IClientWorldEventListener listener){
        clientListeners.put(name,listener);
    }

    public void addServerListener(String name,IServerWorldEventListener listener){
        serverListeners.put(name,listener);
    }

    public <T extends IClientWorldEventListener> T getClientEventListener(String name){
        return (T) clientListeners.get(name);
    }

    public <T extends IServerWorldEventListener> T getServerEventListener(String name){
        return (T) serverListeners.get(name);
    }

    public void onClientHandleEvent(BlockPos pos, int event, PacketArgument[] arguments){
        ClientLevel level = Minecraft.getInstance().level;
        for (IClientWorldEventListener listener : clientListeners.values()) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }

    public void onServerHandleEvent(ServerLevel level,BlockPos pos, int event, PacketArgument[] arguments){
        for (IServerWorldEventListener listener : serverListeners.values()) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }

    public interface IClientWorldEventListener {
        void onHandleEvent(ClientLevel level,BlockPos pos, int event, PacketArgument[] arguments);
    }

    public interface IServerWorldEventListener{
        void onHandleEvent(ServerLevel level, BlockPos pos, int event, PacketArgument[] arguments);
    }

    public static void playClientEvent(Level level, BlockPos pos, int event, PacketArgument... arguments){
        if (level.isClientSide){
            return;
        }
        NetworkUtils.sendToAllPlayers(new ClientMessageWorldPosEvent(pos,event,arguments));
    }

    public static void playClientEvent(Player player, Level level, BlockPos pos, int event, PacketArgument... arguments){
        if (level.isClientSide){
            return;
        }
        NetworkUtils.sendToPlayer((ServerPlayer) player,new ClientMessageWorldPosEvent(pos,event,arguments));
    }

    public static void playServerEvent(Level level, BlockPos pos, int event, PacketArgument... arguments){
        if (!level.isClientSide){
            return;
        }
        NetworkUtils.sendToServer(new ServerMessageWorldPosEvent(pos,event,arguments));
    }

    public static WorldEventHandler getInstance(){
        return INSTANCE;
    }

}
