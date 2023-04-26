package org.astemir.api.client.event;

import net.minecraftforge.eventbus.api.Event;

public class GlobalRenderEvent extends Event {

    private float partialTicks;
    private long nanoTime;


    public GlobalRenderEvent(float partialTicks, long nanoTime) {
        this.partialTicks = partialTicks;
        this.nanoTime = nanoTime;
    }

    public float getDeltaTime(){
        return ((float)nanoTime)+partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public long getNanoTime() {
        return nanoTime;
    }
}
