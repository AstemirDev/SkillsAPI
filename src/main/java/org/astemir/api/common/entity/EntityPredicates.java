package org.astemir.api.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;

import java.util.function.Predicate;

public class EntityPredicates {

    public static Predicate<Animal> animals(){
        return entity -> entity instanceof Animal;
    }

    public static <T extends LivingEntity> Predicate<T> entitiesOfClass(Class<T> entityClass){
        return entity-> entityClass.isInstance(entity);
    }

    public static Predicate<LivingEntity> livingEntities(){
        return entity -> entity.getType() != EntityType.ARMOR_STAND;
    }

    public static Predicate<Entity> exclude(Entity otherEntity){
        return entity -> !entity.getUUID().equals(otherEntity.getUUID());
    }

    public static Predicate<Entity> excludeAll(Entity... entities){
        return entity ->{
            for (Entity otherEntity : entities) {
                if (otherEntity.getUUID().equals(entity.getUUID())){
                    return false;
                }
            }
            return true;
        };
    }
}
