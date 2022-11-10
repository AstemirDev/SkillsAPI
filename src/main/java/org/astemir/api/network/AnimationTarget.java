package org.astemir.api.network;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.common.animation.AnimationFactory;

public enum AnimationTarget {

    ENTITY,BLOCK,ITEM;


    public Level getLevel(AnimationFactory factory){
        if (this == AnimationTarget.ENTITY) {
            Entity entity = (Entity) factory.getAnimated();
            return entity.level;
        }else
        if (this == AnimationTarget.BLOCK){
            BlockEntity entity = (BlockEntity) factory.getAnimated();
            return entity.getLevel();
        }
        return null;
    }
}
