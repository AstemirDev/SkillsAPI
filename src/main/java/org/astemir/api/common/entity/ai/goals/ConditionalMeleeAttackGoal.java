package org.astemir.api.common.entity.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import java.util.function.BooleanSupplier;

public class ConditionalMeleeAttackGoal extends MeleeAttackGoal {

    private BooleanSupplier condition;

    public ConditionalMeleeAttackGoal(PathfinderMob p_25552_, double p_25553_, boolean p_25554_, BooleanSupplier predicate) {
        super(p_25552_, p_25553_, p_25554_);
        this.condition = predicate;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && condition.getAsBoolean();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && condition.getAsBoolean();
    }
}
