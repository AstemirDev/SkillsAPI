package org.astemir.api.common.entity.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

import java.util.function.BooleanSupplier;

public class ConditionalStrollGoal extends RandomStrollGoal {

    private BooleanSupplier condition;

    public ConditionalStrollGoal(PathfinderMob p_25987_, double p_25988_, BooleanSupplier condition) {
        super(p_25987_, p_25988_);
        this.condition = condition;
    }

    public ConditionalStrollGoal(PathfinderMob p_25737_, double p_25738_, int p_25739_, BooleanSupplier condition) {
        super(p_25737_, p_25738_, p_25739_);
        this.condition = condition;
    }

    public ConditionalStrollGoal(PathfinderMob p_25741_, double p_25742_, int p_25743_, boolean p_25744_, BooleanSupplier condition) {
        super(p_25741_, p_25742_, p_25743_, p_25744_);
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
