package org.astemir.api.utils;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.astemir.api.client.ModelUtils;

import java.util.Map;
import java.util.UUID;

public class WorldUtils {

    public static Block[] UNBREAKABLE_BLOCKS = new Block[]{
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.BARRIER,
            Blocks.AIR,
            Blocks.WATER,
            Blocks.LAVA,
            Blocks.END_GATEWAY,
            Blocks.END_PORTAL,
            Blocks.END_PORTAL_FRAME,
            Blocks.CRYING_OBSIDIAN,
            Blocks.VOID_AIR,
            Blocks.STRUCTURE_VOID,
            Blocks.NETHER_PORTAL
    };


    public static boolean isAtStructure(Level level, BlockPos pos, StructureType<?> type){
        if (level instanceof ServerLevel serverLevel){
            Map<Structure, LongSet> structureLongSetMap = serverLevel.structureManager().getAllStructuresAt(pos);
            for (Structure structure : structureLongSetMap.keySet()) {
                if (type == structure.type()){
                    return true;
                }
            }
        }
        return false;
    }

    public static BlockPos getNearestStructurePos(Level level,BlockPos pos,ResourceKey<Structure> structure,int radius,boolean skipKnown){
        if (level instanceof ServerLevel serverLevel) {
            Registry<Structure> registry = serverLevel.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
            Holder<Structure> holder = registry.getHolder(structure).get();
            Pair<BlockPos, Holder<Structure>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, HolderSet.direct(holder), pos, radius, skipKnown);
            if (pair != null){
                if (pair.getFirst() != null){
                    return pair.getFirst();
                }
            }
        }
        return null;
    }

    public static Entity getEntity(UUID uuid, Level level){
        if (!level.isClientSide){
            ServerLevel serverLevel = (ServerLevel) level;
            return serverLevel.getEntity(uuid);
        }
        return null;
    }


    public static void setNight(ServerLevel world){
        long days = world.getDayTime() / 24000;
        if (days < 1){
            days = 1;
        }
        world.setDayTime(days*18000);
    }

    public static void setDay(ServerLevel world){
        long days = world.getDayTime() / 24000;
        if (days < 1){
            days = 1;
        }
        world.setDayTime(days*24000);
    }

    public static boolean isNight(Level world){
        long roundedTime = world.getDayTime() % 24000;
        return roundedTime >= 13000 && roundedTime <= 22000;
    }

    public static boolean isDay(Level world){
        return !isNight(world);
    }

    public static boolean isNightOrDark(Level world,BlockPos pos){
        long roundedTime = world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        int i = world.getBrightness(LightLayer.SKY, pos);
        int j = world.getBrightness(LightLayer.BLOCK, pos);
        int brightness;
        if (night) {
            brightness = j;
        } else {
            brightness = Math.max(i, j);
        }
        if (brightness < 7) {
            return true;
        }
        return false;
    }


    public static boolean canSeePosition(Entity entity, BlockPos pos) {
        Vec3 vec3 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3 vec31 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        if (vec31.distanceTo(vec3) > 128.0D) {
            return false;
        } else {
            return entity.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
        }
    }

    public static boolean isBlock(Block[] blocks,BlockState state) {
        for (Block block : blocks) {
            if (state.is(block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlock(BlockState state,Block... blocks) {
        for (Block block : blocks) {
            if (state.is(block)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isUnbreakableBlock(BlockState state) {
        for (Block unbreakableBlock : UNBREAKABLE_BLOCKS) {
            if (state.is(unbreakableBlock)) {
                return true;
            }
        }
        return false;
    }

    public static BlockPos getNearestAirBlockAbove(Level level, BlockPos pos) {
        BlockPos check = pos;
        while (!level.getBlockState(check).isAir()) {
            if (check.getY() >= level.getMaxBuildHeight()) {
                break;
            }
            check = check.above();
        }
        return check;
    }

    public static boolean isIceBlock(BlockState state) {
        return state.is(Blocks.ICE) || state.is(Blocks.BLUE_ICE) || state.is(Blocks.FROSTED_ICE) || state.is(Blocks.PACKED_ICE);
    }

    public static boolean isEmpty(Level level,BlockPos pos){
        return level.getBlockState(pos).isSolidRender(level,pos);
    }

    public static void setBlock(Level level,BlockPos pos,BlockState state){
        level.setBlock(pos, state, 3);
    }
}
