package org.astemir.api.common.state;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.network.messages.ServerMessageActionSync;
import org.astemir.example.SkillsAPIMod;

public interface IActionListener {

    public ActionStateMachine getActionStateMachine();

    void onActionBegin(ActionController controller,Action state);

    void onActionEnd(ActionController controller,Action state);

    void onActionTick(ActionController controller,Action state,int ticks);

    default void syncClient(){
        if (this instanceof Entity){
            SkillsAPIMod.INSTANCE.getAPINetwork().sendToServer(new ServerMessageActionSync(((Entity)this).getUUID()));
        }else
        if (this instanceof BlockEntity) {
            SkillsAPIMod.INSTANCE.getAPINetwork().sendToServer(new ServerMessageActionSync(((BlockEntity)this).getBlockPos()));
        }
    }

}
