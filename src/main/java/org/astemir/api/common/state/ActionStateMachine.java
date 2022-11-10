package org.astemir.api.common.state;

import net.minecraft.nbt.CompoundTag;

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
        CompoundTag tagDictionary = tag.getCompound("ActionMachine");
        for (int i = 0; i < controllers.size(); i++) {
            if (tagDictionary.contains(String.valueOf(i))) {
                CompoundTag controllerTag = tagDictionary.getCompound(String.valueOf(i));
                ActionController controller = controllers.get(i);
                controller.playAction(controller.getActionById(controllerTag.getInt("Id")), controllerTag.getInt("Ticks"));
            }
        }
    }

    public void write(CompoundTag tag) {
        CompoundTag tagDictionary = new CompoundTag();
        if (tag.contains("ActionMachine")){
            tagDictionary = tag.getCompound("ActionMachine");
        }
        for (int i = 0; i < controllers.size(); i++) {
            CompoundTag controllerTag = new CompoundTag();
            if (controllerTag.contains(String.valueOf(i))){
                controllerTag = tagDictionary.getCompound(String.valueOf(i));
            }
            controllerTag.putInt("Id",controllers.get(i).getActionState().getId());
            controllerTag.putInt("Ticks",controllers.get(i).getTicks());
        }
        tag.put("ActionMachine",tagDictionary);
    }

}
