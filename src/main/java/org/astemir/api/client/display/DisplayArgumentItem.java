package org.astemir.api.client.display;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class DisplayArgumentItem implements IDisplayArgument{

    private ItemStack itemStack;
    private ItemTransforms.TransformType transform;


    public DisplayArgumentItem(ItemStack itemStack, ItemTransforms.TransformType transform) {
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
