package org.astemir.api.common.register;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.awt.*;
import java.util.function.Supplier;

public class ItemRegistry implements IRegistry<Item> {


    public static <T extends EntityType<? extends Mob>> ForgeSpawnEggItem spawnEgg(Supplier<T> type, Color backgroundColor, Color highlightColor, Item.Properties properties){
        return new ForgeSpawnEggItem(type,backgroundColor.hashCode(),highlightColor.hashCode(),properties);
    }
}
