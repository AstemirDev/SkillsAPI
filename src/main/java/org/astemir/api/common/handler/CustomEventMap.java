package org.astemir.api.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.astemir.api.network.PacketArgument;
import java.util.HashMap;
import java.util.Map;

public class CustomEventMap {

    public Map<Integer,EventHandle> map = new HashMap<>();

    public void handleEvent(int id, Level level,BlockPos pos, PacketArgument[] arguments){
        if (map.containsKey(id)) {
            map.get(id).handle(pos,level,arguments);
        }
    }

    public CustomEventMap registerEvent(CustomEvent customEvent,EventHandle handle){
        map.put(customEvent.getValue(),handle);
        return this;
    }

    public CustomEventMap registerEvent(int id,EventHandle handle){
        this.map.put(id,handle);
        return this;
    }

    public static CustomEventMap initialize(){
        return new CustomEventMap();
    }

    public static CustomEvent createEvent(){
        return new CustomEvent();
    }

    public interface EventHandle{
        void handle(BlockPos pos, Level level,PacketArgument[] arguments);
    }
}
