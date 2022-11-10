package org.astemir.example.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.AdvancedItemRenderer;
import org.astemir.api.common.animation.ITESRModel;


import java.util.function.Consumer;

public class MaceItem extends Item implements ITESRModel {


    public MaceItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AdvancedItemRenderer.INSTANCE;
            }
        });
    }
}
