package org.astemir.api.common.world.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.math.vector.Vector3;

import java.util.Map;

public interface ISchematicBuilder {

    default boolean buildSchematicCheckFreeSpace(WESchematic schematic, WorldGenLevel level, BlockPos pos, Vector3 rotation, boolean centered, boolean skipAir){
        if (schematic.isEmptyForPlace(level,pos,rotation,centered,skipAir)){
            buildSchematic(schematic,level,pos,rotation,centered,skipAir);
            return true;
        }
        return false;
    }

    default void buildSchematic(WESchematic schematic, WorldGenLevel level,BlockPos pos, Vector3 rotation,boolean centered, boolean skipAir){
        for (Map.Entry<BlockPos, BlockState> entry : schematic.blocks(pos, rotation,centered, skipAir).entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 19);
        }
    }
}
