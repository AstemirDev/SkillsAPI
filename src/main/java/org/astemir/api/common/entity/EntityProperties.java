package org.astemir.api.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityProperties<T extends Entity> {


    private static Map<EntityType,EntityProperties> entitySettings = new HashMap<>();

    private EntityType.EntityFactory<T> factory;
    private Block[] immuneTo = new Block[]{};
    private MobCategory category;
    private Supplier<AttributeSupplier> attributes;
    private SpawnPlacement placement;
    private boolean serialize = true;
    private boolean summon = true;
    private boolean fireImmune = false;
    private boolean canSpawnFarFromPlayer = false;
    private int clientTrackingRange = 5;
    private int updateInterval = 3;
    private float width = 1;
    private float height = 1;

    public EntityProperties(EntityType.EntityFactory<T> factory, MobCategory category,float width,float height,Supplier<AttributeSupplier> attributes) {
        this.factory = factory;
        this.attributes = attributes;
        this.width = width;
        this.height = height;
        this.category = category;
    }


    public EntityProperties(EntityType.EntityFactory<T> factory, MobCategory category,float width,float height) {
        this.factory = factory;
        this.width = width;
        this.height = height;
        this.category = category;
    }




    public EntityProperties placement(SpawnPlacement placement){
        this.placement = placement;
        return this;
    }

    public EntityProperties<T> noSummon() {
        this.summon = false;
        return this;
    }

    public EntityProperties<T> noSave() {
        this.serialize = false;
        return this;
    }

    public EntityProperties<T> fireImmune() {
        this.fireImmune = true;
        return this;
    }

    public EntityProperties<T> immuneTo(Block... blocks) {
        this.immuneTo = blocks;
        return this;
    }

    public EntityProperties<T> canSpawnFarFromPlayer() {
        this.canSpawnFarFromPlayer = true;
        return this;
    }

    public EntityProperties<T> clientTrackingRange(int range) {
        this.clientTrackingRange = range;
        return this;
    }

    public EntityProperties<T> updateInterval(int interval) {
        this.updateInterval = interval;
        return this;
    }

    public EntityType<T> build(String modId,String id){
        EntityType.Builder builder = EntityType.Builder.of(factory,category).sized(width,height).updateInterval(updateInterval).immuneTo(immuneTo).clientTrackingRange(clientTrackingRange);
        if (canSpawnFarFromPlayer){
            builder = builder.canSpawnFarFromPlayer();
        }
        if (fireImmune){
            builder = builder.fireImmune();
        }
        if (!summon){
            builder = builder.noSummon();
        }
        if (!serialize){
            builder = builder.noSave();
        }
        EntityType type = builder.build(new ResourceLocation(modId,id).toString());
        entitySettings.put(type,this);
        return type;
    }

    public SpawnPlacement getPlacement() {
        return placement;
    }

    public Supplier<AttributeSupplier> getAttributes() {
        return attributes;
    }

    public static class SpawnPlacement{

        private SpawnPlacements.Type placementType;
        private Heightmap.Types heightmapType;
        private SpawnPlacements.SpawnPredicate predicate;

        public SpawnPlacement(SpawnPlacements.Type placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate predicate) {
            this.placementType = placementType;
            this.heightmapType = heightmapType;
            this.predicate = predicate;
        }

        public SpawnPlacements.Type getPlacementType() {
            return placementType;
        }

        public Heightmap.Types getHeightmapType() {
            return heightmapType;
        }

        public SpawnPlacements.SpawnPredicate getPredicate() {
            return predicate;
        }
    }

    public static Map<EntityType, EntityProperties> getEntitySettings() {
        return entitySettings;
    }

    public static EntityProperties getProperties(EntityType type){
        return entitySettings.get(type);
    }
}
