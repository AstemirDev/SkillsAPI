package org.astemir.api.client;

import net.minecraft.resources.ResourceLocation;
import org.astemir.api.io.ResourceUtils;

public class ResourceArray {

    private ResourceLocation[] frames;
    private double speed = 1;


    public ResourceArray(String modId, String path, int count, double speed) {
        this.speed = speed;
        frames = new ResourceLocation[count];
        for (int i = 0;i<count;i++){
            frames[i] = ResourceUtils.loadTexture(modId,String.format(path,i));
        }
    }

    public ResourceLocation getResourceLocation(int ticks){
        double factor = 1/speed;
        int index = (int) ((ticks/factor%frames.length*factor)/factor);
        if (index == frames.length){
            index = 0;
        }
        return frames[index];
    }

    public ResourceArray speed(float speed){
        this.speed = speed;
        return this;
    }
}
