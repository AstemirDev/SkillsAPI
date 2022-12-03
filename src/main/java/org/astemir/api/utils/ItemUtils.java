package org.astemir.api.utils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.astemir.example.common.entity.ExampleModEntities;

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

    public static boolean isFood(ItemStack itemStack){
        return itemStack.getItem().isEdible();
    }

    public static boolean isMeat(LivingEntity consumer,ItemStack itemStack){
        if (itemStack.getItem().isEdible()){
            return itemStack.getItem().getFoodProperties(itemStack,consumer).isMeat();
        }
        return false;
    }

    public static <T extends EntityType<? extends Mob>> ForgeSpawnEggItem spawnEgg(Supplier<T> type, Color backgroundColor, Color highlightColor, Item.Properties properties){
        return new ForgeSpawnEggItem(type,backgroundColor.hashCode(),highlightColor.hashCode(),properties);
    }
}
