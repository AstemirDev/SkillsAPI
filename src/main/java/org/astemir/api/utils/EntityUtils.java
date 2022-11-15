package org.astemir.api.utils;


import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.common.entity.IEventEntity;
import org.astemir.api.math.Vector3;
import org.astemir.api.network.messages.EntityEventMessage;
import org.astemir.example.SkillsAPIMod;
import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {


    public static boolean canBeTargeted(Player player){
        return !player.isCreative() && !player.isSpectator();
    }

    public static boolean hasTarget(Mob mob){
        return isEntityExist(mob.getTarget());
    }

    public static boolean isEntityExist(Entity entity){
        if (entity != null){
            return entity.isAlive();
        }
        return false;
    }

    public static boolean isMoving(LivingEntity entity, float min, float max){
        float limbSwingAmount = 0.0F;
        boolean shouldSit = entity.isPassenger()
                && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());
        if (!shouldSit && entity.isAlive()) {
            limbSwingAmount = Mth.lerp(0.1f, entity.animationSpeedOld, entity.animationSpeed);
            if (limbSwingAmount > 1.0F) {
                limbSwingAmount = 1.0F;
            }
        }
        return !(limbSwingAmount > min && limbSwingAmount < max) || isMovingByPlayer(entity);
    }

    public static boolean isMoving(Entity entity, float min, float max){
        return !(entity.getDeltaMovement().length() > min && entity.getDeltaMovement().length() < max) || isMovingByPlayer(entity);
    }

    public static boolean isMovingByPlayer(Entity entity){
        boolean isMovingByPlayer = false;
        Entity entity1 = entity.getControllingPassenger();
        if (entity1 instanceof Player && entity1 != null){
            Player player = (Player)entity1;
            isMovingByPlayer = player.xxa > 0 || player.zza > 0 || player.xxa  < 0 || player.zza < 0;
        }
        return isMovingByPlayer;
    }


    public static boolean isEntityLookingAtDegree(Entity entity,LivingEntity looker,double degree){
        degree *= 1 + (looker.distanceTo(entity) * 0.1);
        return isEntityLookingAt(entity,looker,degree);
    }

    public static boolean isEntityLookingAt(Entity entity,LivingEntity looker) {
        return isEntityLookingAt(entity,looker,0.025D);
    }

    public static boolean isEntityLookingAt(Entity entity,LivingEntity looker, double d) {
        Vec3 vec3 = looker.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(entity.getX() - looker.getX(), entity.getEyeY() - looker.getEyeY(), entity.getZ() - looker.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - d / d0 ? looker.hasLineOfSight(entity) : false;
    }

    public static <T extends Entity> List<T> getEntities(Class<T> entityClass, Level level, BlockPos pos, int radius, Predicate<? super T> predicate){
        return level.getEntitiesOfClass(entityClass,new AABB(pos).inflate(radius),predicate);
    }

    public static <T extends Entity> List<T> getEntities(Class<T> entityClass, Level level, BlockPos pos, int radius){
        return level.getEntitiesOfClass(entityClass,new AABB(pos).inflate(radius));
    }

    public static <T extends Entity> T getEntity(Class<T> entityClass, Level level, BlockPos pos, int radius, Predicate<? super T> predicate){
        List<T> list = getEntities(entityClass,level,pos,radius,predicate);
        if (list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public static <T extends Entity> T getEntity(Class<T> entityClass, Level level, BlockPos pos, int radius){
        List<T> list = getEntities(entityClass,level,pos,radius);
        if (list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public static BlockHitResult getBlockHit(Entity entity,double distance){
        return getBlockHit(entity,0,0,distance);
    }

    public static BlockHitResult getBlockHit(Entity entity,float xRot,float yRot,double distance){
        float f = entity.getXRot()+xRot;
        float f1 = entity.getYRot()+yRot;
        Vec3 eyePos = entity.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vec31 = eyePos.add((double)f6 * distance, (double)f5 * distance, (double)f7 * distance);
        return entity.level.clip(new ClipContext(eyePos, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
    }


    public static <T extends Entity & IEventEntity> void invokeEntityClientEvent(T entity,int event){
        if (entity.level.isClientSide){
            return;
        }
        SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->entity),new EntityEventMessage(entity.getId(),event));
    }


    public static void breakNearbyBlocks(LivingEntity entity, Vector3 size, int yOffset, int blockBreakChance) {
        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(entity.level, entity) && !entity.level.isClientSide) {
            int j1 = Mth.floor(entity.getY());
            int i2 = Mth.floor(entity.getX());
            int j2 = Mth.floor(entity.getZ());
            boolean flag = false;
            for (int j = (int) -size.x; j <= size.x; ++j) {
                for (int k2 = (int) -size.z; k2 <= size.z; ++k2) {
                    for (int k = -yOffset; k <= size.y; ++k) {
                        int l2 = i2 + j;
                        int l = j1 + k;
                        int i1 = j2 + k2;
                        BlockPos blockpos = new BlockPos(l2, l, i1);
                        BlockState blockstate = entity.level.getBlockState(blockpos);
                        if (!WorldUtils.isUnbreakableBlock(blockstate) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(entity, blockpos, blockstate)) {
                            if (RandomUtils.doWithChance(blockBreakChance)) {
                                flag = entity.level.destroyBlock(blockpos, true, entity) || flag;
                            }
                        }
                    }
                }
            }
            if (flag) {
                entity.level.levelEvent((Player) null, 1022, entity.blockPosition(), 0);
            }
        }
    }
}
