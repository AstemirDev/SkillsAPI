package org.astemir.api.common.animation;

import net.minecraft.resources.ResourceLocation;

public class AnimatedResourceLocation {

    private ResourceLocation[] frames;
    private double speed = 1;


    public AnimatedResourceLocation(String modId,String path,int count,double speed) {
        this.speed = speed;
        frames = new ResourceLocation[count];
        for (int i = 0;i<count;i++){
            frames[i] = new ResourceLocation(modId,String.format(path,i));
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

}
