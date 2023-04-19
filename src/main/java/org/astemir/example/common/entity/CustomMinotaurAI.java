package org.astemir.example.common.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.astemir.api.common.entity.ai.tasks.AITask;
import org.astemir.api.common.entity.ai.tasks.AITaskAttack;
import org.astemir.api.common.entity.ai.tasks.AITaskTimer;
import org.astemir.api.common.entity.ai.triggers.TaskExecution;
import org.astemir.api.common.entity.ai.triggers.TaskTrigger;

public class CustomMinotaurAI {

    public static final AITask wanderingTask(EntityExampleMinotaur minotaur){
        return new AITask(0).layer(0).setGoal(new WaterAvoidingRandomStrollGoal(minotaur,0.8f));
    }

    public static final AITask attackTask(EntityExampleMinotaur minotaur){
        return new AITaskAttack(1, 0.8f, 3, 20, target -> minotaur.getAnimationFactory().play(EntityExampleMinotaur.ANIMATION_ATTACK)){
            @Override
            public boolean canUseAttack() {
                return super.canUseAttack();
            }
        }.layer(0);
    }

    public static final AITask appleEatingHappiness(EntityExampleMinotaur minotaur) {
        return new AITaskTimer(3, 120) {
            @Override
            public void onUpdate() {
                minotaur.getAnimationFactory().play(EntityExampleMinotaur.ANIMATION_FURY);
                if (getTicks() % 6 > 3) {
                    getEntity().getLookControl().setLookAt(getEntity().getX(), getEntity().getY() + 4, getEntity().getZ());
                } else {
                    getEntity().getLookControl().setLookAt(getEntity().getX(), getEntity().getY() - 2, getEntity().getZ());
                }
                if (getTicks() % 5 == 0) {
                    ai().jump(this);
                }
                minotaur.playClientEvent(EntityExampleMinotaur.EVENT_IN_LOVE);
            }

            @Override
            public boolean onInteract(Player player, InteractionHand hand, ItemStack itemStack, Direction face) {
                if (itemStack.is(Items.APPLE)) {
                    itemStack.shrink(1);
                    minotaur.playClientEvent(EntityExampleMinotaur.EVENT_FEED);
                    return true;
                }
                return false;
            }
        }.layer(0).interrupts(0, 1).execution(TaskExecution.ONCE).trigger(TaskTrigger.INTERACTION);
    }

    public static final AITask targetFind(EntityExampleMinotaur minotaur){
        return new AITask(10).layer(1).setGoal(new NearestAttackableTargetGoal(minotaur, LivingEntity.class,false));
    }
}
