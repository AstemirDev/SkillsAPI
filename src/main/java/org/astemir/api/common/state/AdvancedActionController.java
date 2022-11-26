package org.astemir.api.common.state;




public class AdvancedActionController<T extends IActionListener> extends ActionController<T>{


    public AdvancedActionController(T owner, String name, Action... actions) {
        this(owner,name,0,actions);
    }

    public AdvancedActionController(T owner, String name, int delay, Action... actions) {
        super(owner,name,delay,actions);
    }

    @Override
    public void playAction(Action action){
        playAction(action,0);
    }

    @Override
    public void playAction(Action action,int ownDelay) {
        if ((!is(action) || action.isCanOverrideSelf())) {
            if (!getLevel().isClientSide) {
                if (action.getLength() > 0) {
                    setActionWithoutSync(action, action.getLength() + getDelay() + ownDelay);
                } else {
                    setActionWithoutSync(action, -1);
                }
                sendUpdatePacket();
            }
            getOwner().onActionBegin(this,action);
        }
    }

    @Override
    @Deprecated
    public void setAction(Action action,int ticks) {
        if ((!is(getActionState()) || getActionState().isCanOverrideSelf())) {
            if (!getLevel().isClientSide) {
                if (ticks > 0) {
                    setActionWithoutSync(action, ticks + getDelay());
                } else {
                    setActionWithoutSync(action, -1);
                }
                sendUpdatePacket();
            }
            getOwner().onActionBegin(this,action);
        }
    }

    @Override
    public void update(){
        Action previous = getActionState();
        if (!isNoAction()) {
            if (getTicks() > 0) {
                getOwner().onActionTick(this,getActionState(), getTicks());
                if (!getLevel().isClientSide) {
                    sendUpdatePacket();
                    setTick(getTicks()-1);
                }
            }else {
                if (getTicks() != -1) {
                    getOwner().onActionEnd(this,getActionState());
                    if (!getLevel().isClientSide) {
                        if (is(previous) && getTicks() != -1) {
                            playAction(NO_ACTION);
                        }
                    }
                }
            }
        }
    }
}
