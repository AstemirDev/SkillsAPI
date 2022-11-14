package org.astemir.api.common.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import org.astemir.api.network.messages.ActionControllerMessage;
import org.astemir.example.SkillsAPIMod;


public class ActionController<T extends IActionListener> {


    public static final Action NO_ACTION = new Action(-1,"noAction",-1);

    private Action[] actions;
    private Action action = NO_ACTION;
    private T owner;
    private String name;
    private int actionTick = 0;
    private int delay = 0;


    public ActionController(T owner,String name,Action... actions) {
        this(owner,name,0,actions);
    }

    public ActionController(T owner, String name,int delay,Action... actions) {
        this.owner = owner;
        this.delay = delay;
        this.name = name;
        this.actions = actions;
        owner.getActionStateMachine().addController(this);
    }

    public void playAction(Action action){
        playAction(action,0);
    }

    public void playAction(Action action,int ownDelay) {
        if (!getLevel().isClientSide && (!is(action) || action.isCanOverrideSelf())) {
            if (action.getLength() > 0) {
                setActionWithoutSync(action, action.getLength() +delay+ownDelay);
            }else{
                setActionWithoutSync(action, -1);
            }
            sendUpdatePacket();
            owner.onActionBegin(this,action);
        }
    }

    @Deprecated
    public void setAction(Action action,int ticks) {
        if (!getLevel().isClientSide && (!is(action) || action.isCanOverrideSelf())) {
            if (ticks > 0) {
                setActionWithoutSync(action, ticks + delay);
            }else{
                setActionWithoutSync(action, -1);
            }
            sendUpdatePacket();
            owner.onActionBegin(this,action);
        }
    }

    public void setActionWithoutSync(Action action,int ticks){
        this.action = action;
        this.actionTick = ticks;
    }

    public String getName() {
        return name;
    }

    public int getTicks(){
        return actionTick;
    }

    public void setTick(int ticks){
        this.actionTick = ticks;
    }

    public void setNoState(){
        playAction(NO_ACTION);
    }

    public boolean isNoAction(){
        return getActionState() == NO_ACTION;
    }

    public boolean is(Action state){
        return getActionState() == state;
    }

    public void update(){
        if (!getLevel().isClientSide) {
            if (!isNoAction()) {
                if (actionTick > 0) {
                    owner.onActionTick(this,action, actionTick);
                    sendUpdatePacket();
                    actionTick--;
                }else {
                    if (actionTick != -1) {
                        owner.onActionEnd(this,action);
                        if (is(action) && actionTick != -1) {
                            playAction(NO_ACTION);
                        }
                    }
                }
            }
        }
    }

    public Level getLevel(){
        if (owner instanceof Entity){
            return ((Entity)owner).level;
        }
        if (owner instanceof BlockEntity){
            return ((BlockEntity)owner).getLevel();
        }
        return null;
    }

    public void sendUpdatePacket(){
        if (owner instanceof Entity) {
            Entity entity = (Entity)owner;
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new ActionControllerMessage(entity.getId(), owner.getActionStateMachine().getIdByName(getName()), action.getId(),actionTick));
        }

        if (owner instanceof BlockEntity){
            BlockEntity blockEntity = (BlockEntity) owner;
            BlockPos pos = blockEntity.getBlockPos();
            SkillsAPIMod.INSTANCE.getAPINetwork().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(),pos.getY(),pos.getZ(),128,blockEntity.getLevel().dimension())), new ActionControllerMessage(pos, owner.getActionStateMachine().getIdByName(getName()), action.getId(),actionTick));
        }
    }

    public Action[] getActions() {
        return actions;
    }

    public Action getActionById(int id){
        if (id == -1){
            return NO_ACTION;
        }
        for (Action action : actions) {
            if (action.getId() == id){
                return action;
            }
        }
        return NO_ACTION;
    }

    public Action getActionState() {
        return action;
    }
}
