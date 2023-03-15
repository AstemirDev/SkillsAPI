package org.astemir.example.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.SunRendererItem;
import org.astemir.api.common.misc.ICustomRendered;


import java.util.function.Consumer;

public class ItemExampleMace extends Item implements ICustomRendered {


    public ItemExampleMace() {
        super(new Item.Properties());
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SunRendererItem.INSTANCE;
            }
        });
    }
}
