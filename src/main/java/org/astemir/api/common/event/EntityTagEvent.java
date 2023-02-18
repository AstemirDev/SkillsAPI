package org.astemir.api.common.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class EntityTagEvent extends Event {

    private Entity entity;
    private CompoundTag compoundTag;

    public EntityTagEvent(Entity entity,CompoundTag compoundTag) {
        this.entity = entity;
        this.compoundTag = compoundTag;
    }

    public Entity getEntity() {
        return entity;
    }

    public CompoundTag getCompoundTag() {
        return compoundTag;
    }

    public static class Load extends EntityTagEvent{

        public Load(Entity entity, CompoundTag compoundTag) {
            super(entity, compoundTag);
        }
    }

    public static class Save extends EntityTagEvent{

        public Save(Entity entity, CompoundTag compoundTag) {
            super(entity, compoundTag);
        }
    }
}
