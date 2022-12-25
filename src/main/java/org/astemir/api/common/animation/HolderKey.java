package org.astemir.api.common.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HolderKey {

    private int id = -1;
    private BlockPos pos;
    private HolderTarget target;

    public HolderKey(IAnimated animated) {
        if (animated instanceof Entity){
            this.id = ((Entity)animated).getId();
            this.target = HolderTarget.ENTITY;
        }
        if (animated instanceof BlockEntity){
            this.pos = ((BlockEntity)animated).getBlockPos();
            this.target = HolderTarget.BLOCK;
        }
    }

    public HolderKey(int key) {
        this.id = key;
        target = HolderTarget.ENTITY;
    }

    public HolderKey(BlockPos key) {
        this.pos = key;
        target = HolderTarget.BLOCK;
    }

    public boolean equalsKey(Object object){
        if (object == null){
            return false;
        }
        if (id != -1) {
            if (object instanceof HolderKey){
                return id == (((HolderKey)object).getId());
            }
            if (object instanceof Integer) {
                return id == (int)object;
            }
        }
        if (pos != null) {
            if (object instanceof HolderKey){
                BlockPos otherPos = ((HolderKey)object).getPos();
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

    public static HolderKey read(FriendlyByteBuf buf){
        HolderTarget target = buf.readEnum(HolderTarget.class);
        switch (target){
            case ENTITY:{
                return new HolderKey(buf.readInt());
            }
            case BLOCK:{
                return new HolderKey(buf.readBlockPos());
            }
        }
        return null;
    }

    public boolean isEntity(){
        return target == HolderTarget.ENTITY;
    }

    public boolean isBlock(){
        return target == HolderTarget.BLOCK;
    }

    public HolderTarget getTarget() {
        return target;
    }

    public int getId() {
        return id;
    }

    public BlockPos getPos() {
        return pos;
    }
}
