package org.astemir.api.data.samples.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

public enum BlockType {

    SLAB(BlockTags.SLABS),
    BUTTON(BlockTags.BUTTONS),
    STAIRS(BlockTags.STAIRS),
    DOOR(BlockTags.DOORS),
    TRAPDOOR(BlockTags.TRAPDOORS),
    FENCE(BlockTags.FENCES),
    FENCE_GATE(BlockTags.FENCE_GATES),
    PRESSURE_PLATE(BlockTags.PRESSURE_PLATES),
    WALL(BlockTags.WALLS),
    DIRT(BlockTags.DIRT),
    SAND(BlockTags.SAND),
    ICE(BlockTags.ICE),
    CROPS(BlockTags.CROPS),
    FLOWER_POT(BlockTags.FLOWER_POTS),
    SMALL_FLOWER(BlockTags.FLOWERS,BlockTags.SMALL_FLOWERS),
    TALL_FLOWER(BlockTags.FLOWERS,BlockTags.TALL_FLOWERS),
    PORTAL(BlockTags.PORTALS),
    SIGN(BlockTags.SIGNS,BlockTags.STANDING_SIGNS),
    WALL_SIGN(BlockTags.WALL_SIGNS),
    ORE(BlockTags.STONE_ORE_REPLACEABLES),
    DEEPSLATE_ORE(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
    LOGS(BlockTags.LOGS,BlockTags.LOGS_THAT_BURN,BlockTags.OVERWORLD_NATURAL_LOGS),
    FIREPROOF_LOGS(BlockTags.LOGS,BlockTags.OVERWORLD_NATURAL_LOGS),
    WOODEN_BUTTONS(BlockTags.BUTTONS,BlockTags.WOODEN_BUTTONS),
    WOODEN_SLAB(BlockTags.SLABS,BlockTags.WOODEN_SLABS),
    WOODEN_DOOR(BlockTags.DOORS,BlockTags.WOODEN_DOORS),
    WOODEN_TRAPDOOR(BlockTags.TRAPDOORS,BlockTags.WOODEN_TRAPDOORS),
    WOODEN_PRESSURE_PLATES(BlockTags.PRESSURE_PLATES,BlockTags.WOODEN_PRESSURE_PLATES),
    WOODEN_FENCE(BlockTags.FENCES,BlockTags.WOODEN_FENCES);

    private TagKey[] tags;

    BlockType(TagKey... tags) {
        this.tags = tags;
    }

    public TagKey[] getTags() {
        return tags;
    }
}
