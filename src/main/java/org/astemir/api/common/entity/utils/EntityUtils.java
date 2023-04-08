package org.astemir.api.common.entity.utils;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.common.entity.IEventEntity;
import org.astemir.api.math.vector.Vector2;
import org.astemir.api.math.vector.Vector3;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.network.messages.ClientMessageEntityEvent;
import org.astemir.api.network.messages.ServerMessageEntityEvent;
import org.astemir.api.network.NetworkUtils;
import org.astemir.api.math.random.RandomUtils;
import org.astemir.api.common.world.WorldUtils;

import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {


    public static void damageEntity(LivingEntity damager,Entity target){
        damageEntity(damager,target,1,false);
    }

    public static void damageEntity(LivingEntity damager,Entity target,boolean breakShield){
        damageEntity(damager,target,1,breakShield);
    }

    public static void damageEntity(LivingEntity damager,Entity target,float multiplier){
        damageEntity(damager,target,multiplier,false);
    }

    public static void damageEntity(LivingEntity damager,Entity target,float multiplier,boolean breakShield){
        float f = (float)damager.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float)damager.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(damager.getMainHandItem(), ((LivingEntity)target).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(damager);
        }
        int i = EnchantmentHelper.getFireAspect(damager);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }
        boolean flag = target.hurt(DamageSource.mobAttack(damager), f*multiplier);
        if (target instanceof Player) {
            Player player = (Player)target;
            ItemStack mainHand = damager.getMainHandItem();
            ItemStack usingItem = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
            if (((!mainHand.isEmpty() && mainHand.getItem() instanceof AxeItem) || breakShield) && usingItem.is(Items.SHIELD)) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                player.stopUsingItem();
                damager.level.broadcastEntityEvent(player, (byte)30);
            }
        }
        if (flag) {
            if (f1 > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity)target).knockback(f1 * 0.5F, Mth.sin(damager.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(damager.getYRot() * ((float)Math.PI / 180F)));
                damager.setDeltaMovement(damager.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            damager.doEnchantDamageEffects(damager, target);
            damager.setLastHurtMob(target);
        }
    }

    public static Vector3 directionTo(Entity from,Entity to){
        return Vector3.from(from.position()).direction(Vector3.from(to.position()));
    }

    public static Entity rotate(Entity entity,Vector3 direction){
        Vector2 rot = direction.mul(1,0,1).yawPitchDeg();
        entity.setYRot(-rot.x);
        entity.setYBodyRot(-rot.x);
        entity.setXRot(-rot.y);
        entity.yRotO = -rot.x;
        entity.xRotO = -rot.y;
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = -rot.x;
            livingEntity.yHeadRot = -rot.x;
        }
        return entity;
    }

    public static Entity setPositionRotation(Entity entity,Vector3 position,float yaw,float pitch){
        entity.moveTo(position.x,position.y,position.z,yaw,pitch);
        return entity;
    }

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

    public static float getMotionPotential(Entity entity) {
        float f = Math.min((float) entity.getDeltaMovement().lengthSqr()*100, 1.0F);
        return f;
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


    public static <T extends Entity> List<T> getEntities(EntityType<T> type, Level level, BlockPos pos, float radius, Predicate<? super T> predicate){
        return level.getEntities(type,new AABB(pos).inflate(radius),predicate);
    }

    public static <T extends Entity> List<T> getEntities(Class<T> entityClass, Level level, BlockPos pos, float radius, Predicate<? super T> predicate){
        return level.getEntitiesOfClass(entityClass,new AABB(pos).inflate(radius),predicate);
    }

    public static <T extends Entity> List<T> getEntities(Class<T> entityClass, Level level, BlockPos pos, float radius){
        return level.getEntitiesOfClass(entityClass,new AABB(pos).inflate(radius));
    }

    public static <T extends Entity> T getEntity(Class<T> entityClass, Level level, BlockPos pos, float radius, Predicate<? super T> predicate){
        List<T> list = getEntities(entityClass,level,pos,radius,predicate);
        if (list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public static <T extends Entity> T getEntity(Class<T> entityClass, Level level, BlockPos pos, float radius){
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


    public static <T extends Entity & IEventEntity> void playClientEvent(T entity, int event, PacketArgument... arguments){
        if (entity.level.isClientSide){
            return;
        }
        NetworkUtils.sendToAllPlayers(new ClientMessageEntityEvent(entity.getId(),event,arguments));
    }

    public static <T extends Entity & IEventEntity> void playClientEvent(T entity, ServerPlayer serverPlayer,int event, PacketArgument... arguments){
        if (entity.level.isClientSide){
            return;
        }
        NetworkUtils.sendToPlayer(serverPlayer,new ClientMessageEntityEvent(entity.getId(),event,arguments));
    }


    public static <T extends Entity & IEventEntity> void playServerEvent(T entity, int event, PacketArgument... arguments){
        if (!entity.level.isClientSide){
            return;
        }
        NetworkUtils.sendToServer(new ServerMessageEntityEvent(entity.getId(),event,arguments));
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
