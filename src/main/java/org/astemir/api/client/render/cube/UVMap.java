package org.astemir.api.client.render.cube;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class UVMap {

    private Map<Direction,CubeUV> uvMap = new HashMap<>();

    public UVMap put(Direction direction,CubeUV uv){
        uvMap.put(direction,uv);
        return this;
    }

    public CubeUV get(Direction direction){
        return uvMap.get(direction);
    }

    public boolean has(Direction direction){
        return uvMap.containsKey(direction);
    }
}
