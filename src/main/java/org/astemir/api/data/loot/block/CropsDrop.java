package org.astemir.api.data.loot.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CropsDrop extends BlockDrop {

    private int minAge = 1;
    private IntegerProperty ageProperty;

    public CropsDrop(Block block, ItemLike crops, ItemLike seeds, int minAge, IntegerProperty ageProperty) {
        super(block, BlockDropType.CROPS);
        this.minAge = minAge;
        this.ageProperty = ageProperty;
        otherDrop("crops",crops);
        otherDrop("seeds",seeds);
    }

    public int getMinAge() {
        return minAge;
    }

    public ItemLike getCrops() {
        return getDrop("crops");
    }

    public ItemLike getSeeds() {
        return getDrop("seeds");
    }

    public IntegerProperty getAgeProperty() {
        return ageProperty;
    }
}
