package org.astemir.api.client.display;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class ItemDisplayArgument {

    private ItemStack itemStack;
    private ItemTransforms.TransformType transform;


    public ItemDisplayArgument(ItemStack itemStack, ItemTransforms.TransformType transform) {
        this.itemStack = itemStack;
        this.transform = transform;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemTransforms.TransformType getTransform() {
        return transform;
    }

}
