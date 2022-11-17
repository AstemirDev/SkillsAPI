package org.astemir.api.common.state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

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

    public void addController(ActionController controller){
        this.controllers.add(controller);
    }

    public LinkedList<ActionController> getControllers() {
        return controllers;
    }

    public void read(CompoundTag tag){
        ListTag listTag = tag.getList("ActionMachine",10);
        for (int i = 0; i < controllers.size(); i++) {
            CompoundTag controllerTag = listTag.getCompound(i);
            ActionController controller = controllers.get(i);
            controller.playAction(controller.getActionById(controllerTag.getInt("Id")), controllerTag.getInt("Ticks"));
        }
    }

    public void write(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (int i = 0; i < controllers.size(); i++) {
            CompoundTag controllerTag = new CompoundTag();
            controllerTag.putInt("Id",controllers.get(i).getActionState().getId());
            controllerTag.putInt("Ticks",controllers.get(i).getTicks());
            listTag.add(i,controllerTag);
        }
        tag.put("ActionMachine",listTag);
    }

}
