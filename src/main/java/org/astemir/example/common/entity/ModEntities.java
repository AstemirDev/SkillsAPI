package org.astemir.example.common.entity;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.common.entity.EntityProperties;
import org.astemir.api.common.RegisterHelper;
import org.astemir.example.ExampleAPIMod;

public class ModEntities extends RegisterHelper{

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleAPIMod.MOD_ID);
    public static final RegistryObject<EntityType<EntityMinotaur>> MINOTAUR = register(ENTITIES,ExampleAPIMod.MOD_ID,"minotaur",new EntityProperties(EntityMinotaur::new,MobCategory.AMBIENT,1,2,()->EntityMinotaur.createAttributes().build()));
    public static final RegistryObject<EntityType> SHARK_BOAT = ENTITIES.register("shark_boat",()->EntityType.Builder.of(EntitySharkBoat::new,MobCategory.MISC).sized(1,1).build("shark_boat"));

}
