package org.astemir.api.common.entity.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

import java.util.function.BooleanSupplier;

public class ConditionalWAStrollGoal extends WaterAvoidingRandomStrollGoal {

    private BooleanSupplier condition;

    public ConditionalWAStrollGoal(PathfinderMob p_25987_, double p_25988_, BooleanSupplier condition) {
        super(p_25987_, p_25988_);
        this.condition = condition;
    }

    public ConditionalWAStrollGoal(PathfinderMob p_25990_, double p_25991_, float p_25992_, BooleanSupplier condition) {
        super(p_25990_, p_25991_, p_25992_);
        this.condition = condition;
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
