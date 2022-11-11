package org.astemir.api.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class EntityData<V> {

    public static Map<EntityDataSerializer,SerializationFunc> serializationMap = new HashMap<>();

    static {
        serializationMap.put(EntityDataSerializers.INT, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                tag.putInt(data.getName(), (int) data.get(entity));
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,tag.getInt(data.getName()));
            }
        });

        serializationMap.put(EntityDataSerializers.BOOLEAN, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                tag.putBoolean(data.getName(), (boolean) data.get(entity));
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,tag.getBoolean(data.getName()));
            }
        });

        serializationMap.put(EntityDataSerializers.FLOAT, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                tag.putFloat(data.getName(), (float) data.get(entity));
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,tag.getFloat(data.getName()));
            }
        });

        serializationMap.put(EntityDataSerializers.BLOCK_POS, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                BlockPos pos = (BlockPos) data.get(entity);
                if (pos != null) {
                    tag.put(data.getName(),NbtUtils.writeBlockPos(pos));
                }
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                BlockPos pos = NbtUtils.readBlockPos(tag.getCompound(data.getName()));
                data.set(entity,pos);
            }
        });

        serializationMap.put(EntityDataSerializers.OPTIONAL_UUID, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                Optional<UUID> optional = (Optional<UUID>) data.get(entity);
                if (optional.isPresent()){
                    tag.putUUID(data.getName(),optional.get());
                }
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                UUID uuid = tag.getUUID(data.getName());
                data.set(entity,Optional.of(uuid));
            }
        });

        serializationMap.put(EntityDataSerializers.STRING, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                tag.putString(data.getName(), (String) data.get(entity));
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,tag.getString(data.getName()));
            }
        });

        serializationMap.put(EntityDataSerializers.COMPOUND_TAG, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                tag.put(data.getName(),(CompoundTag)data.get(entity));
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,tag.get(data.getName()));
            }
        });

        serializationMap.put(EntityDataSerializers.ITEM_STACK, new SerializationFunc() {
            @Override
            public void serialize(Entity entity, EntityData data, CompoundTag tag) {
                CompoundTag itemTag = new CompoundTag();
                ((ItemStack)data.get(entity)).save(itemTag);
                tag.put(data.getName(),itemTag);
            }

            @Override
            public void deserialize(Entity entity, EntityData data, CompoundTag tag) {
                data.set(entity,ItemStack.of(tag.getCompound(data.getName())));
            }
        });
    }

    private EntityDataAccessor dataAccessor;
    private V defaultValue;
    private String name;

    public EntityData(Class<? extends Entity> entityClass, String name, EntityDataSerializer<V> dataSerializer, V defaultValue) {
        this.name = name;
        this.dataAccessor = SynchedEntityData.defineId(entityClass,dataSerializer);
        this.defaultValue = defaultValue;
    }

    public void register(Entity entity){
        entity.getEntityData().define(dataAccessor,defaultValue());
    }

    public void set(Entity entity,Object value){
        entity.getEntityData().set(dataAccessor,value);
    }

    public V get(Entity entity){
        if (entity.getEntityData().get(dataAccessor) == null){
            return defaultValue();
        }else {
            return (V) entity.getEntityData().get(dataAccessor);
        }
    }

    public V defaultValue() {
        return defaultValue;
    }

    public EntityDataAccessor getDataAccessor() {
        return dataAccessor;
    }

    public String getName() {
        return name;
    }

    public void load(Entity entity,CompoundTag tag){
        if (tag != null) {
            if (tag.contains(getName())) {
                serializationMap.get(dataAccessor.getSerializer()).deserialize(entity, this, tag);
            }
        }
    }

    public void save(Entity entity,CompoundTag tag){
        if (tag != null) {
            serializationMap.get(dataAccessor.getSerializer()).serialize(entity, this, tag);
        }
    }


    public interface SerializationFunc{

        void serialize(Entity entity, EntityData data,CompoundTag tag);

        void deserialize(Entity entity,EntityData data,CompoundTag tag);
    }
}
