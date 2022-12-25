package org.astemir.api.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
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
import org.jetbrains.annotations.Nullable;

public abstract class SAEntityAnimal extends Animal implements IAnimated, IActionListener,IEventEntity, ISARendered {

    private ActionStateMachine actionStateMachine = new ActionStateMachine();

    protected SAEntityAnimal(EntityType<? extends Animal> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
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
        for (ActionController controller : actionStateMachine.getControllers()) {
            controller.update();
        }
    }


    public void invokeEntityClientEvent(int event,PacketArgument... arguments){
        EntityUtils.invokeEntityClientEvent(this,event,arguments);
    }


    public void invokeEntityServerEvent(int event,PacketArgument... arguments){
        EntityUtils.invokeEntityServerEvent(this,event,arguments);
    }

    @Override
    public void onHandleClientEvent(int event, PacketArgument[] arguments) {
        onHandleClientEvent(event);
    }

    public void onHandleClientEvent(int event) {
    }

    @Override
    public void onHandleServerEvent(int event, PacketArgument[] arguments) {
        onHandleServerEvent(event);
    }

    public void onHandleServerEvent(int event) {
    }


    @Override
    public void onActionBegin(ActionController controller, Action state) {
    }

    @Override
    public void onActionEnd(ActionController controller, Action state) {
    }

    @Override
    public void onActionTick(ActionController controller, Action state, int ticks) {
    }

    @Override
    public long getTicks() {
        return tickCount;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationTick(Animation animation, int tick) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }


    @Override
    public ActionStateMachine getActionStateMachine() {
        return actionStateMachine;
    }
}
