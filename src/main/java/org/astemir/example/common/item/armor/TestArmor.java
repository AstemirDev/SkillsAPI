package org.astemir.example.common.item.armor;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import org.astemir.api.common.item.SAArmorItem;


public class TestArmor extends SAArmorItem {

    public TestArmor(EquipmentSlot p_40387_) {
        super(ArmorMaterials.LEATHER, p_40387_, new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

}
