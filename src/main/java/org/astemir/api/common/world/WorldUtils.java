package org.astemir.api.common.world;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.monster.hoglin.HoglinAi;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.astemir.api.client.ModelUtils;
import org.astemir.api.math.vector.Vector3;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class WorldUtils {


    public static <T extends Entity> T createEntity(EntityType type, Level level, Vector3 position, float yaw, float pitch){
        T entity = (T) type.create(level);
        entity.moveTo(position.x,position.y,position.z,yaw,pitch);
        return entity;
    }

    public static <T extends Entity> T createEntity(EntityType type,Level level,Vector3 position){
        T entity = (T) type.create(level);
        entity.moveTo(position.x,position.y,position.z);
        return entity;
    }


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

    public static BlockPos getNearestStructurePos(Level level, BlockPos pos, ResourceKey<Biome> biome, int radiusX,int radiusY,int radiusZ){
        if (level instanceof ServerLevel serverLevel) {
            Registry<Biome> registry = serverLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
            Holder<Biome> holder = registry.getHolder(biome).get();
            Pair<BlockPos, Holder<Biome>> pair = serverLevel.findClosestBiome3d((p)->p == holder,pos,radiusX,radiusY,radiusZ);
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
        if (!state.is(BlockTags.WITHER_IMMUNE)) {
            return state.getFluidState().isEmpty();
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

    public static boolean isEmpty(Level level,BlockPos pos){
        return level.getBlockState(pos).isSolidRender(level,pos);
    }

    public static void setBlock(Level level,BlockPos pos,BlockState state){
        level.setBlock(pos, state, 3);
    }
}
