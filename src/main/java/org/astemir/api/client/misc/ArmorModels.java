package org.astemir.api.client.misc;

import net.minecraft.world.item.Item;
import org.astemir.api.client.wrapper.ArmorModelWrapper;

import java.util.HashMap;
import java.util.Map;

public class ArmorModels {

    public static Map<Item, ArmorModelWrapper> armorModels = new HashMap<>();


    public static void addModel(Item item,ArmorModelWrapper wrapper){
        armorModels.put(item,wrapper);
    }

    public static ArmorModelWrapper getModel(Item item){
        return armorModels.get(item);
    }
}
