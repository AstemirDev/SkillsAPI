package org.astemir.api.common.entity.ai;

import net.minecraft.world.entity.Entity;

public abstract class EntityTask {

    private Entity entity;
    private int delay = 0;
    private int ticksBegin = 0;
    private boolean shouldStopAfterRun = false;
    private boolean cancelled = false;


    public EntityTask(Entity entity, int delay) {
        this.entity = entity;
        this.delay = delay;
        this.ticksBegin = entity.tickCount;
    }

    public void update() {
        if (entity.tickCount >= ticksBegin + delay && !cancelled) {
            ticksBegin = entity.tickCount;
            run();
            if (shouldStopAfterRun) {
                cancelled = true;
                end();
            }
        }
    }

    public void stop(){
        cancelled = true;
        end();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void wakeUp() {
        cancelled = false;
        this.ticksBegin = entity.tickCount;
    }


    public EntityTask sleep() {
        cancelled = true;
        return this;
    }


    public EntityTask shouldStopAfterRun() {
        shouldStopAfterRun = true;
        return this;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public abstract void run();

    public void end(){};
}
