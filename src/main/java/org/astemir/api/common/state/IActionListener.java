package org.astemir.api.common.state;


public interface IActionListener {

    public ActionStateMachine getActionStateMachine();

    void onActionBegin(ActionController controller,Action state);

    void onActionEnd(ActionController controller,Action state);

    void onActionTick(ActionController controller,Action state,int ticks);
}
