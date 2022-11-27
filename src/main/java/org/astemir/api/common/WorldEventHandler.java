package org.astemir.api.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.network.messages.BlockEventMessage;
import org.astemir.example.SkillsAPIMod;

import java.util.ArrayList;
import java.util.List;

public class WorldEventHandler {

    public static WorldEventHandler INSTANCE = new WorldEventHandler();

    private List<IWorldEventListener> listeners = new ArrayList<>();


    public static WorldEventHandler getInstance(){
        return INSTANCE;
    }

    public void addListener(IWorldEventListener listener){
        listeners.add(listener);
    }


    public void onHandleEvent(BlockPos pos, int event, PacketArgument[] arguments){
        ClientLevel level = Minecraft.getInstance().level;
        for (IWorldEventListener listener : listeners) {
            listener.onHandleEvent(level,pos,event,arguments);
        }
    }

    public interface IWorldEventListener{
        void onHandleEvent(ClientLevel level,BlockPos pos, int event, PacketArgument[] arguments);
    }
}
