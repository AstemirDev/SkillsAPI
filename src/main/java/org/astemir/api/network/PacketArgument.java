package org.astemir.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.math.Color;
import org.astemir.api.utils.NetworkUtils;

import java.util.UUID;

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
            case VEC3 -> NetworkUtils.writeVec3(buf, (Vec3) value);
            case DOUBLE -> buf.writeDouble((double)value);
            case FLOAT -> buf.writeFloat((float) value);
            case INT -> buf.writeInt((int)value);
            case STRING -> buf.writeUtf((String)value);
            case UUID -> buf.writeUUID((UUID)value);
            case COLOR -> NetworkUtils.writeColor(buf,(Color)value);
        }
    }

    public static PacketArgument read(FriendlyByteBuf buf){
        ArgumentType type = buf.readEnum(ArgumentType.class);
        switch (type){
            case INT -> {
                return new PacketArgument(type,buf.readInt());
            }
            case DOUBLE -> {
                return new PacketArgument(type, buf.readDouble());
            }
            case FLOAT -> {
                return new PacketArgument(type, buf.readFloat());
            }
            case STRING ->{
                return new PacketArgument(type,buf.readUtf());
            }
            case VEC3 -> {
                return new PacketArgument(type,NetworkUtils.readVec3(buf));
            }
            case UUID ->{
                return new PacketArgument(type,buf.readUUID());
            }
            case COLOR ->{
                return new PacketArgument(type,NetworkUtils.readColor(buf));
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

    public String asString(){return (String)value;}

    public double asDouble(){
        return (double)value;
    }

    public Object getValue() {
        return value;
    }

    public Color getColor(){return (Color)value;}

    public ArgumentType getType() {
        return type;
    }

    public static PacketArgument create(ArgumentType type,Object value){
        return new PacketArgument(type,value);
    }

    public enum ArgumentType{
        VEC3,FLOAT,DOUBLE,INT,STRING,UUID,COLOR;
    }

}
