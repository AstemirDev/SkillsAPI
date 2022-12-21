package org.astemir.api.common.state;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.SkillsAPI;
import org.astemir.api.network.messages.ServerMessageActionSync;

public interface IActionListener {

    public ActionStateMachine getActionStateMachine();

    void onActionBegin(ActionController controller,Action state);

    void onActionEnd(ActionController controller,Action state);

    void onActionTick(ActionController controller,Action state,int ticks);

    default void syncClient(){
        if (this instanceof Entity){
            SkillsAPI.API_NETWORK.sendToServer(new ServerMessageActionSync(((Entity)this).getId()));
        }else
        if (this instanceof BlockEntity) {
            SkillsAPI.API_NETWORK.sendToServer(new ServerMessageActionSync(((BlockEntity)this).getBlockPos()));
        }
    }

}
