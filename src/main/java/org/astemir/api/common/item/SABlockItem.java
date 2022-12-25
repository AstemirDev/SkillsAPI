package org.astemir.api.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.api.common.animation.ISARendered;

import java.util.function.Consumer;

public class SABlockItem extends BlockItem implements ISARendered {

    public SABlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AdvancedRendererItem.INSTANCE;
            }
        });
    }
}
