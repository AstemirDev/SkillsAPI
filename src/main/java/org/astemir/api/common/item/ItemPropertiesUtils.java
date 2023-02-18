package org.astemir.api.common.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemPropertiesUtils {

    public static void registerShieldProperties(Item item){
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174590_, p_174591_, p_174592_, p_174593_) -> p_174592_ != null && p_174592_.isUsingItem() && p_174592_.getUseItem() == p_174590_ ? 1.0F : 0.0F);
    }
}
