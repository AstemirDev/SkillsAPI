package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class IAnimatedID {

    private UUID uuid;
    private BlockPos pos;

    public IAnimatedID(IAnimated animated) {
        if (animated instanceof Entity){
            uuid = ((Entity)animated).getUUID();
        }
        if (animated instanceof BlockEntity){
            pos = ((BlockEntity)animated).getBlockPos();
        }
    }

    public IAnimatedID(Object key) {
        if (key instanceof UUID){
            uuid = (UUID) key;
        }
        if (key instanceof BlockPos){
            pos = (BlockPos) key;
        }
    }

    public boolean equalsKey(Object object){
        if (object == null){
            return false;
        }

        if (uuid != null) {
            if (object instanceof IAnimatedID){
                return uuid.equals(((IAnimatedID)object).getUniqueID());
            }
            if (object instanceof UUID) {
                return uuid.equals(object);
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

    public UUID getUniqueID() {
        return uuid;
    }

    public BlockPos getPos() {
        return pos;
    }
}
