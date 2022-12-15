package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class IAnimatedID {

    private int id = -1;
    private BlockPos pos;

    public IAnimatedID(IAnimated animated) {
        if (animated instanceof Entity){
            id = ((Entity)animated).getId();
        }
        if (animated instanceof BlockEntity){
            pos = ((BlockEntity)animated).getBlockPos();
        }
    }

    public IAnimatedID(Object key) {
        if (key instanceof Integer){
            id = (int) key;
        }
        if (key instanceof BlockPos){
            pos = (BlockPos) key;
        }
    }

    public boolean equalsKey(Object object){
        if (object == null){
            return false;
        }

        if (id != -1) {
            if (object instanceof IAnimatedID){
                return id == (((IAnimatedID)object).getId());
            }
            if (object instanceof Integer) {
                return id == (int)object;
            }
        }
        if (pos != null) {
            if (object instanceof IAnimatedID){
                BlockPos otherPos = ((IAnimatedID)object).getPos();
                if (otherPos != null) {
                    return otherPos.getX() == pos.getX() && otherPos.getY() == pos.getY() && otherPos.getZ() == pos.getZ();
                }
            }
            if (object instanceof BlockPos) {
                BlockPos blockPos = ((BlockPos) object);
                return blockPos.getX() == pos.getX() && blockPos.getY() == pos.getY() && blockPos.getZ() == pos.getZ();
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }
}
