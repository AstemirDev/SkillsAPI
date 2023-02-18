package org.astemir.example.common.item;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.example.SkillsAPI;
import org.astemir.example.common.item.armor.ArmorItemExample;


public class ExampleModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SkillsAPI.MOD_ID);
    public static final RegistryObject<Item> MACE = ITEMS.register("mace",()->new ItemExampleMace());

    public static final RegistryObject<Item> TEST_HELMET = ITEMS.register("test_helmet",()->new ArmorItemExample(EquipmentSlot.HEAD));
    public static final RegistryObject<Item> TEST_CHESTPLATE = ITEMS.register("test_chestplate",()->new ArmorItemExample(EquipmentSlot.CHEST));
    public static final RegistryObject<Item> TEST_LEGGINGS = ITEMS.register("test_leggings",()->new ArmorItemExample(EquipmentSlot.LEGS));
    public static final RegistryObject<Item> TEST_BOOTS = ITEMS.register("test_boots",()->new ArmorItemExample(EquipmentSlot.FEET));

}
