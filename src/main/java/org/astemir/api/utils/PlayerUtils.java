package org.astemir.api.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {

    public static boolean isEquipped(Entity entity,Item item, EquipmentSlot slot){
        if (entity instanceof Player player){
            return player.getItemBySlot(slot).is(item);
        }
        return false;
    }

    public static void cooldownItem(Entity entity, Item item, int time){
        if (entity instanceof Player player){
            player.getCooldowns().addCooldown(item,time);
        }
    }

    public static boolean isOnCooldown(Entity entity, Item item){
        if (entity instanceof Player player){
            return player.getCooldowns().isOnCooldown(item);
        }
        return false;
    }

    public static ItemStack getRandomItem(Player player, int maxAttempts){
        ItemStack random = ItemStack.EMPTY;
        int i = 0;
        while(random.isEmpty() && i < maxAttempts){
            random = player.getInventory().getItem(RandomUtils.randomInt(player.getInventory().items.size()));
            i++;
        }
        return random;
    }

}
