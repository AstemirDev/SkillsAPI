package org.astemir.api.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.AdvancedRendererItem;
import org.astemir.api.common.animation.ISARendered;

import java.util.function.Consumer;

public class SAItem extends Item implements ISARendered {

    public SAItem(Properties p_41383_) {
        super(p_41383_);
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
