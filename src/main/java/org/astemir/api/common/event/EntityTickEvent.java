package org.astemir.api.common.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class EntityTickEvent extends Event {

    private Entity entity;
    private long tickCount;

    public EntityTickEvent(Entity entity, long tickCount) {
        this.entity = entity;
        this.tickCount = tickCount;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getTickCount() {
        return tickCount;
    }

}
