package org.astemir.api.client.registry;

import net.minecraft.world.item.Item;
import org.astemir.api.client.wrapper.SkillsWrapperArmor;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelsRegistry {

    public static Map<Item, SkillsWrapperArmor> armorModels = new HashMap<>();


    public static void addModel(Item item, SkillsWrapperArmor wrapper){
        armorModels.put(item,wrapper);
    }

    public static SkillsWrapperArmor getModel(Item item){
        return armorModels.get(item);
    }
}
