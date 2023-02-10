package org.astemir.api.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ISARendered;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.common.state.IActionListener;

public abstract class SAEntityHorse extends AbstractHorse implements IAnimated, IActionListener,IEventEntity, ISARendered {

    private ActionStateMachine actionStateMachine = new ActionStateMachine();

    protected SAEntityHorse(EntityType<? extends AbstractHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        actionStateMachine.write(p_21484_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        actionStateMachine.read(p_21450_);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        for (ActionController controller : actionStateMachine.getControllers()) {
            controller.update();
        }
    }

    @Override
    public long getTicks() {
        return tickCount;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ActionStateMachine getActionStateMachine() {
        return actionStateMachine;
    }
}
