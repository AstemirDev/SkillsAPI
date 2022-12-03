package org.astemir.api.client;

import net.minecraft.world.item.Item;
import org.astemir.api.client.wrapper.AbstractModelWrapperArmor;

import java.util.HashMap;
import java.util.Map;

public class ArmorModels {

    public static Map<Item, AbstractModelWrapperArmor> armorModels = new HashMap<>();


    public static void addModel(Item item, AbstractModelWrapperArmor wrapper){
        armorModels.put(item,wrapper);
    }

    public static AbstractModelWrapperArmor getModel(Item item){
        return armorModels.get(item);
    }
}
