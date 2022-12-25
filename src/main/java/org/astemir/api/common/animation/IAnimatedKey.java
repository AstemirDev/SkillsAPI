package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class IAnimatedKey {

    private int id = -1;
    private BlockPos pos;
    private AnimationTarget target;

    public IAnimatedKey(IAnimated animated) {
        if (animated instanceof Entity){
            id = ((Entity)animated).getId();
            target = AnimationTarget.ENTITY;
        }
        if (animated instanceof BlockEntity){
            pos = ((BlockEntity)animated).getBlockPos();
            target = AnimationTarget.BLOCK;
        }
    }

    public IAnimatedKey(int key) {
        id = id;
        target = AnimationTarget.ENTITY;
    }

    public IAnimatedKey(BlockPos key) {
        this.pos = key;
        target = AnimationTarget.BLOCK;
    }

    public boolean equalsKey(Object object){
        if (object == null){
            return false;
        }
        if (id != -1) {
            if (object instanceof IAnimatedKey){
                return id == (((IAnimatedKey)object).getId());
            }
            if (object instanceof Integer) {
                return id == (int)object;
            }
        }
        if (pos != null) {
            if (object instanceof IAnimatedKey){
                BlockPos otherPos = ((IAnimatedKey)object).getPos();
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

    public void write(FriendlyByteBuf buf){
        buf.writeEnum(target);
        switch (target){
            case ENTITY:{
                buf.writeInt(id);
                break;
            }
            case BLOCK:{
                buf.writeBlockPos(pos);
                break;
            }
        }
    }

    public static IAnimatedKey read(FriendlyByteBuf buf){
        AnimationTarget target = buf.readEnum(AnimationTarget.class);
        switch (target){
            case ENTITY:{
                return new IAnimatedKey(buf.readInt());
            }
            case BLOCK:{
                return new IAnimatedKey(buf.readBlockPos());
            }
        }
        return null;
    }

    public boolean isEntity(){
        return target == AnimationTarget.ENTITY;
    }

    public boolean isBlock(){
        return target == AnimationTarget.BLOCK;
    }

    public AnimationTarget getTarget() {
        return target;
    }

    public int getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }
}
