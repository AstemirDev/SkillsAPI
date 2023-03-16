package org.astemir.api.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.astemir.api.client.render.SkillsRendererItem;
import org.astemir.api.common.misc.ICustomRendered;

import java.util.function.Consumer;

public class SkillsBlockItem extends BlockItem implements ICustomRendered {

    public SkillsBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SkillsRendererItem.INSTANCE;
            }
        });
    }
}
