package org.astemir.api.client.registry;

import net.minecraft.world.item.Item;
import org.astemir.api.client.wrapper.MWArmor;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelsRegistry {

    public static Map<Item, MWArmor> armorModels = new HashMap<>();


    public static void addModel(Item item, MWArmor wrapper){
        armorModels.put(item,wrapper);
    }

    public static MWArmor getModel(Item item){
        return armorModels.get(item);
    }
}
