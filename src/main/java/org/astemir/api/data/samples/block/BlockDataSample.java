package org.astemir.api.data.samples.block;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import org.astemir.api.data.blockstate.BlockStateHolder;
import org.astemir.api.data.blockstate.BlockStateType;
import org.astemir.api.data.loot.block.BlockDrop;
import org.astemir.api.data.samples.AbstractDataSample;

public class BlockDataSample extends AbstractDataSample<Block> {

    private BlockTool digTool;
    private BlockType blockType;
    private BlockStateHolder blockState;
    private BlockDrop blockDrop;

    public BlockDataSample(Block instance) {
        super(instance);
    }

    public BlockDataSample blockState(BlockStateHolder blockState) {
        this.blockState = blockState;
        return this;
    }

    public BlockDataSample blockState(BlockDrop drop) {
        this.blockDrop = drop;
        return this;
    }

    public BlockDataSample blockState(BlockStateType blockState) {
        this.blockState = new BlockStateHolder(getInstance(),blockState);
        return this;
    }

    public BlockDataSample digTool(BlockTool digTool) {
        this.digTool = digTool;
        for (TagKey tag : digTool.getTags()) {
            addTag(tag);
        }
        return this;
    }

    public BlockDataSample blockType(BlockType type) {
        this.blockType = type;
        for (TagKey tag : type.getTags()) {
            addTag(tag);
        }
        return this;
    }

    public BlockTool getDigTool() {
        return digTool;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public BlockStateHolder getBlockState() {
        return blockState;
    }

    public BlockDrop getBlockDrop() {
        return blockDrop;
    }
}
