package org.astemir.api.common.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.common.animation.AnimationFactory;
import org.astemir.api.network.AnimationTarget;
import org.astemir.api.network.messages.ClientActionSyncMessage;
import org.astemir.api.network.messages.ClientAnimationSyncMessage;
import org.astemir.example.SkillsAPIMod;

import java.util.LinkedList;

public class ActionStateMachine {

    private LinkedList<ActionController> controllers = new LinkedList<>();


    public int getIdByName(String name){
        for (int i = 0; i < this.controllers.size(); i++) {
            if (name.equals(controllers.get(i).getName())){
                return i;
            }
        }
        return 0;
    }

    public void update(){
        for (ActionController controller : controllers) {
            controller.update();
        }
    }

    public void addController(ActionController controller){
        this.controllers.add(controller);
    }

    public LinkedList<ActionController> getControllers() {
        return controllers;
    }

    public void read(CompoundTag tag){
        CompoundTag actionMachineTag = tag.getCompound("ActionMachine");
        for (ActionController controller : controllers) {
            if (actionMachineTag.contains(controller.getName())) {
                CompoundTag controllerTag = actionMachineTag.getCompound(controller.getName());
                if (!controllerTag.isEmpty()) {
                    Action action = controller.getActionByName(controllerTag.getString("Name"));
                    controller.playAction(action, controllerTag.getInt("Ticks"));
                }
            }
        }
    }

    public void write(CompoundTag tag) {
        CompoundTag actionMachineTag = new CompoundTag();
        for (ActionController controller : controllers) {
            if (!controller.isNoAction()) {
                CompoundTag controllerTag = new CompoundTag();
                controllerTag.putString("Action",controller.getActionState().getName());
                controllerTag.putInt("Ticks",controller.getTicks());
                actionMachineTag.put(controller.getName(),controllerTag);
            }
        }
        tag.put("ActionMachine",actionMachineTag);
    }

}
