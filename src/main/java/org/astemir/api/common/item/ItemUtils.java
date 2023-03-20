package org.astemir.api.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {


    public static boolean damageItem(ItemStack itemStack, int damage){
        if (itemStack.getDamageValue()+damage < itemStack.getMaxDamage()) {
            itemStack.setDamageValue(itemStack.getDamageValue() + damage);
            return true;
        }
        return false;
    }

    public static <T extends LivingEntity> void damageItem(T owner, ItemStack itemStack, InteractionHand hand,int damage){
        itemStack.hurtAndBreak(damage,owner,(t)->{
            owner.broadcastBreakEvent(hand);
        });
    }

    public static <T extends LivingEntity> void damageItem(T owner, ItemStack itemStack, EquipmentSlot slot, int damage){
        itemStack.hurtAndBreak(damage,owner,(t)->{
            owner.broadcastBreakEvent(slot);
        });
    }

    public static boolean isFood(ItemStack itemStack){
        return itemStack.getItem().isEdible();
    }

    public static boolean isMeat(LivingEntity consumer,ItemStack itemStack) {
        if (itemStack.getItem().isEdible()) {
            return itemStack.getItem().getFoodProperties(itemStack, consumer).isMeat();
        }
        return false;
    }
}
