package org.astemir.api.data.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import org.astemir.api.data.blockstate.BlockStateHolder;

import static org.astemir.api.io.ResourceUtils.*;

public class BlockModelUtils {


    public static BlockModelBuilder createTintedCross(BlockStateProvider provider, BlockStateHolder stateHolder, String topOrBottom) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString()+"_"+topOrBottom)
                .parent(createModelFile("block/tinted_cross"))
                .texture("cross", stateHolder.getMaterial(topOrBottom)).renderType("cutout");
    }

    public static BlockModelBuilder createTintedCross(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString())
                .parent(createModelFile("block/tinted_cross"))
                .texture("cross", stateHolder.getMaterial("texture")).renderType("cutout");
    }

    public static BlockModelBuilder createCrossBlock(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString())
                .parent(createModelFile("block/cross"))
                .texture("cross", stateHolder.getMaterial("texture")).renderType("cutout");
    }

    public static BlockModelBuilder createLeavesBlock(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString())
                .parent(createModelFile("block/leaves"))
                .texture("all", stateHolder.getMaterial("texture")).renderType("cutout_mipped");
    }

    public static BlockModelBuilder createCubeMirroredAll(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString()+"_mirrored")
                .parent(createModelFile("block/cube_mirrored_all"))
                .texture("all", stateHolder.getMaterial("texture"));
    }

    public static BlockModelBuilder createEmpty(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString())
                .texture("particle", stateHolder.getMaterial("texture"));
    }

    public static BlockModelBuilder createWallInventory(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString()+"_inventory")
                .parent(createModelFile("block/wall_inventory"))
                .texture("wall", stateHolder.getMaterial("texture"));
    }

    public static BlockModelBuilder createButtonInventory(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString()+"_inventory")
                .parent(createModelFile("block/button_inventory"))
                .texture("texture", stateHolder.getMaterial("texture"));
    }

    public static BlockModelBuilder createFenceInventory(BlockStateProvider provider, BlockStateHolder stateHolder) {
        ResourceLocation location = getBlockLocation(stateHolder.getBlock());
        return provider.models().getBuilder(location.toString()+"_inventory")
                .parent(createModelFile("block/fence_inventory"))
                .texture("texture", stateHolder.getMaterial("texture"));
    }
}
