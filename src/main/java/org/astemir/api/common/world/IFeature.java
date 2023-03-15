package org.astemir.api.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.stream.Stream;

public interface IFeature<T extends FeatureConfiguration>{

    default BlockPos surfacePosition(FeaturePlaceContext<T> context,int x,int z){
        BlockPos pos = context.origin();
        int y = context.level().getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + x, pos.getZ() + z);
        return new BlockPos(pos.getX() + x, y, pos.getZ() + z);
    }

    default Stream<BlockPos> sphere(BlockPos position, int radius, float a, float b) {
        int j = radius;
        int k = radius / 2;
        int l = radius;
        float f = (float) (j + k + l) * a + b;
        return BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::immutable).filter((blockPos)->{
            if (blockPos.distSqr(position) <= (double) (f * f)) {
                return true;
            }else{
                return false;
            }
        });
    }

    default void placePlant(WorldGenLevel level, BlockState state, BlockPos pos){
        if (state.getBlock() instanceof DoublePlantBlock) {
            level.setBlock(pos, state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 4);
            if (level.isEmptyBlock(pos.above())) {
                level.setBlock(pos.above(), state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 4);
            }
        }else{
            level.setBlock(pos, state, 4);
        }
    }
}
