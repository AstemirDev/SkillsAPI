package org.astemir.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PacketArgument {

    private Object value;
    private ArgumentType type;

    public PacketArgument(ArgumentType type,Object value) {
        this.value = value;
        this.type = type;
    }

    public void write(FriendlyByteBuf buf){
        buf.writeEnum(type);
        switch (type){
            case VEC3 -> PacketUtils.writeVec3(buf, (Vec3) value);
            case DOUBLE -> buf.writeDouble((double)value);
            case FLOAT -> buf.writeFloat((float) value);
            case INT -> buf.writeInt((int)value);
        }
    }

    public static PacketArgument read(FriendlyByteBuf buf){
        ArgumentType type = buf.readEnum(ArgumentType.class);
        switch (type){
            case VEC3 -> {
                return new PacketArgument(type,PacketUtils.readVec3(buf));
            }
            case FLOAT -> {
                return new PacketArgument(type, buf.readFloat());
            }
            case DOUBLE -> {
                return new PacketArgument(type, buf.readDouble());
            }
            case INT -> {
                return new PacketArgument(type,buf.readInt());
            }
        }
        return null;
    }

    public float asFloat(){
        return (float) value;
    }

    public Vec3 asVec3(){
        return (Vec3) value;
    }

    public int asInt(){return (int)value;}

    public double asDouble(){
        return (double)value;
    }

    public Object getValue() {
        return value;
    }


    public ArgumentType getType() {
        return type;
    }

    public enum ArgumentType{
        VEC3,FLOAT,DOUBLE,INT;
    }

}
