package org.astemir.api.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.SunRendererItem;
import org.astemir.api.common.misc.ICustomRendered;

import java.util.function.Consumer;

public class SkillsItem extends Item implements ICustomRendered {

    public SkillsItem(Properties p_41383_) {
        super(p_41383_);
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
