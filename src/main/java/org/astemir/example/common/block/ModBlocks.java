package org.astemir.example.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.astemir.example.SkillsAPIMod;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SkillsAPIMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkillsAPIMod.MOD_ID);

    public static RegistryObject<Block> COSMIC_BEACON = BLOCKS.register("cosmic_beacon", BlockCosmicBeacon::new);
    public static RegistryObject<BlockEntityType<? extends BlockEntityCosmicBeacon>> COSMIC_BEACON_ENTITY = TILE_ENTITIES.register("cosmic_beacon", ()->BlockEntityType.Builder.of(BlockEntityCosmicBeacon::new,COSMIC_BEACON.get()).build(null));
}
