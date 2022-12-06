package org.astemir.api.data.samples;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class BlockDataSample extends AbstractDataSample<Block> {

    public BlockDataSample(Block instance) {
        super(instance);
    }

    public BlockDataSample woodenSlab(){
        addTag(BlockTags.SLABS);
        addTag(BlockTags.WOODEN_SLABS);
        return this;
    }

    public BlockDataSample button(){
        addTag(BlockTags.BUTTONS);
        return this;
    }

    public BlockDataSample woodenButton(){
        addTag(BlockTags.WOODEN_BUTTONS);
        return this;
    }


    public BlockDataSample slab(){
        addTag(BlockTags.SLABS);
        return this;
    }

    public BlockDataSample pickaxeMineable(){
        addTag(BlockTags.MINEABLE_WITH_PICKAXE);
        return this;
    }

    public BlockDataSample axeMineable(){
        addTag(BlockTags.MINEABLE_WITH_AXE);
        return this;
    }

    public BlockDataSample hoeMinable(){
        addTag(BlockTags.MINEABLE_WITH_HOE);
        return this;
    }


    public BlockDataSample shovelMinable(){
        addTag(BlockTags.MINEABLE_WITH_SHOVEL);
        return this;
    }
}
