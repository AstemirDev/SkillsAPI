package org.astemir.api.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.animation.ISARendered;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.common.state.IActionListener;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.utils.EntityUtils;

public abstract class SAEntityMonster extends Monster implements IAnimated, IActionListener,IEventEntity, ISARendered {

    private ActionStateMachine actionStateMachine = new ActionStateMachine();

    protected SAEntityMonster(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        actionStateMachine.update();
    }

    @Override
    public long getTicks() {
        return tickCount;
    }

    @Override
    public ActionStateMachine getActionStateMachine() {
        return actionStateMachine;
    }
}
