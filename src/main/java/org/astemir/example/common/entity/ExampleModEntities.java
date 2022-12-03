package org.astemir.example.common.entity;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.api.common.entity.EntityProperties;
import org.astemir.api.utils.RegisterUtils;
import org.astemir.example.SkillsAPIMod;

public class ExampleModEntities extends RegisterUtils {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkillsAPIMod.MOD_ID);
    public static final RegistryObject<EntityType<EntityExampleMinotaur>> MINOTAUR = register(ENTITIES, SkillsAPIMod.MOD_ID,"minotaur",new EntityProperties(EntityExampleMinotaur::new,MobCategory.AMBIENT,1,2,()-> Zombie.createAttributes().build()));
    public static final RegistryObject<EntityType> SHARK_BOAT = ENTITIES.register("shark_boat",()->EntityType.Builder.of(EntityExampleSharkBoat::new,MobCategory.MISC).sized(1,1).build("shark_boat"));

}
