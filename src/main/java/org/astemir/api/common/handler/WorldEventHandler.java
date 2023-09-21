package org.astemir.api.common.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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

    private Map<String,ClientEventHandler> clientListeners = new HashMap<>();
    private Map<String,ServerEventHandler> serverListeners = new HashMap<>();

    public void addClientListener(ResourceLocation name, ClientEventHandler listener){
        clientListeners.put(name.toString(),listener);
    }

    public void addServerListener(ResourceLocation name,ServerEventHandler listener){
        serverListeners.put(name.toString(),listener);
    }

    public <T extends ClientEventHandler> T getClientEventListener(String name){
        return (T) clientListeners.get(name);
    }

    public <T extends ServerEventHandler> T getServerEventListener(String name){
        return (T) serverListeners.get(name);
    }

    public void onClientHandleEvent(BlockPos pos, int event, PacketArgument[] arguments){
        ClientLevel level = Minecraft.getInstance().level;
        for (ClientEventHandler listener : clientListeners.values()) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }

    public void onServerHandleEvent(ServerLevel level,BlockPos pos, int event, PacketArgument[] arguments){
        for (ServerEventHandler listener : serverListeners.values()) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }


    public static void playClientEvent(Level level, BlockPos pos, CustomEvent event, PacketArgument... arguments){
        playClientEvent(level,pos,event.getValue(),arguments);
    }

    public static void playClientEvent(Player player, Level level, BlockPos pos, CustomEvent event, PacketArgument... arguments){
        playClientEvent(player,level,pos,event.getValue(),arguments);
    }

    public static void playServerEvent(Level level, BlockPos pos, CustomEvent event, PacketArgument... arguments){
        playServerEvent(level,pos,event.getValue(),arguments);
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

    public static void registerClientHandler(ResourceLocation location,ClientEventHandler handler){
        getInstance().addClientListener(location,handler);
    }

    public static void registerServerHandler(ResourceLocation location,ServerEventHandler handler){
        getInstance().addServerListener(location,handler);
    }

    public static WorldEventHandler getInstance(){
        return INSTANCE;
    }

}
