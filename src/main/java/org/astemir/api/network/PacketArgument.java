package org.astemir.api.network;

import net.minecraft.nbt.CompoundTag;
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
            case VEC3 -> NetworkUtils.writeVec3(buf, asVec3());
            case DOUBLE -> buf.writeDouble(asDouble());
            case FLOAT -> buf.writeFloat(asFloat());
            case INT -> buf.writeInt(asInt());
            case STRING -> buf.writeUtf(asString());
            case UUID -> buf.writeUUID(asUUID());
            case COLOR -> NetworkUtils.writeColor(buf,asColor());
            case NBT -> buf.writeNbt(asNBT());
            case BOOL -> buf.writeBoolean(asBoolean());
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
            case BOOL -> {
                return new PacketArgument(type,buf.readBoolean());
            }
            case COLOR ->{
                return new PacketArgument(type,NetworkUtils.readColor(buf));
            }
            case NBT -> {
                return new PacketArgument(type,buf.readAnySizeNbt());
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

    public boolean asBoolean(){return (boolean)value;}

    public String asString(){return (String)value;}

    public double asDouble(){
        return (double)value;
    }

    public Color asColor(){return (Color)value;}

    public UUID asUUID(){return (UUID)value;}

    public CompoundTag asNBT(){return (CompoundTag) value;}


    public Object getValue() {
        return value;
    }

    public ArgumentType getType() {
        return type;
    }

    public static PacketArgument create(ArgumentType type,Object value){
        return new PacketArgument(type,value);
    }

    public enum ArgumentType{
        VEC3,FLOAT,DOUBLE,INT,STRING,UUID,COLOR,NBT,BOOL
    }

}
