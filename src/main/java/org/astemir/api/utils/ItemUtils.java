package org.astemir.api.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ItemUtils {

    public static ItemStack getRandomItem(Player player,int maxAttempts){
        ItemStack random = ItemStack.EMPTY;
        int i = 0;
        while(random.isEmpty() && i < maxAttempts){
            random = player.getInventory().getItem(RandomUtils.randomInt(player.getInventory().items.size()));
            i++;
        }
        return random;
    }


    public static boolean isMeat(LivingEntity livingEntity,ItemStack stack){
        if (stack.getItem().isEdible()){
            return stack.getItem().getFoodProperties(stack,livingEntity).isMeat();
        }
        return false;
    }

    public static ForgeSpawnEggItem spawnEgg(Supplier type, Color a, Color b, Item.Properties properties){
        return new ForgeSpawnEggItem(type,a.hashCode(),b.hashCode(),properties);
    }
}
