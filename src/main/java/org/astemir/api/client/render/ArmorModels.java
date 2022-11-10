package org.astemir.api.client.render;

import net.minecraft.world.item.Item;
import org.astemir.api.client.model.ArmorModelWrapper;

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
