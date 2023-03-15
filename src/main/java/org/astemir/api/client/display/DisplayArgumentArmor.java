package org.astemir.api.client.display;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DisplayArgumentArmor implements IDisplayArgument{


    private LivingEntity livingEntity;
    private ItemStack itemStack;
    private EquipmentSlot equipmentSlot;


    public DisplayArgumentArmor(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot) {
        this.livingEntity = livingEntity;
        this.itemStack = itemStack;
        this.equipmentSlot = equipmentSlot;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }
}
