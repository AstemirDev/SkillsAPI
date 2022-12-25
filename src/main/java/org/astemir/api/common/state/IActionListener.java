package org.astemir.api.common.state;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.SkillsAPI;
import org.astemir.api.common.animation.HolderKey;
import org.astemir.api.network.messages.ServerMessageActionSync;
import org.astemir.api.utils.NetworkUtils;

public interface IActionListener {

    public ActionStateMachine getActionStateMachine();

    void onActionBegin(ActionController controller,Action state);

    void onActionEnd(ActionController controller,Action state);

    void onActionTick(ActionController controller,Action state,int ticks);

    default void syncClient(){
        if (this instanceof Entity entity){
            NetworkUtils.sendToServer(new ServerMessageActionSync(new HolderKey(entity.getId())));
        }else
        if (this instanceof BlockEntity blockEntity) {
            NetworkUtils.sendToServer(new ServerMessageActionSync(new HolderKey(blockEntity.getBlockPos())));
        }
    }

}
