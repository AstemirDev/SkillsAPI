package org.astemir.api.common.animation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public enum HolderTarget {

    ENTITY,BLOCK,ITEM;


    public Level getLevel(AnimationFactory factory){
        if (this == HolderTarget.ENTITY) {
            Entity entity = (Entity) factory.getAnimated();
            return entity.level;
        }else
        if (this == HolderTarget.BLOCK){
            BlockEntity entity = (BlockEntity) factory.getAnimated();
            return entity.getLevel();
        }
        return null;
    }
}
