package org.astemir.api.data.samples.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public enum BlockTool {
    AXE(BlockTags.MINEABLE_WITH_AXE),PICKAXE(BlockTags.MINEABLE_WITH_PICKAXE),SHOVEL(BlockTags.MINEABLE_WITH_SHOVEL),HOE(BlockTags.MINEABLE_WITH_HOE);


    private TagKey[] tags;

    BlockTool(TagKey... tags) {
        this.tags = tags;
    }

    public TagKey[] getTags() {
        return tags;
    }
}
