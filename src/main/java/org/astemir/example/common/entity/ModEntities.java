package org.astemir.example.common.entity;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.example.ExampleAnimationAPIMod;

@Mod.EventBusSubscriber(modid = ExampleAnimationAPIMod.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleAnimationAPIMod.MOD_ID);
    public static final RegistryObject<EntityType<EntityMinotaur>> MINOTAUR = ENTITIES.register("minotaur",()->register("minotaur", EntityMinotaur::new, MobCategory.AMBIENT,1,2));
    public static final RegistryObject<EntityType> SHARK_BOAT = ENTITIES.register("shark_boat",()->EntityType.Builder.of(EntitySharkBoat::new,MobCategory.MISC).sized(1,1).build("shark_boat"));

    public static final EntityType register(String name, EntityType.EntityFactory entity, MobCategory classification, float width, float height){
        EntityType<? extends Mob> type = EntityType.Builder.of(entity, classification).sized(width,height).build(new ResourceLocation(name).toString());
        return type;
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent e){
        e.put(MINOTAUR.get(), EntityMinotaur.createAttributes().build());
    }

}
