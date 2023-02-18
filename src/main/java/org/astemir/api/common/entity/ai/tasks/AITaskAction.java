package org.astemir.api.common.entity.ai.tasks;

import org.astemir.api.common.action.Action;
import org.astemir.api.common.action.ActionController;

public class AITaskAction extends AITask implements IActionTask{

    private Action action;
    private ActionController controller;

    public AITaskAction(int id, ActionController controller,Action action) {
        super(id);
        this.controller = controller;
        this.action = action;
    }

    @Override
    public boolean canStart() {
        return super.canStart() && controller.is(action);
    }

    @Override
    public boolean canContinue() {
        return super.canContinue() && controller.is(action);
    }

    public Action getAction() {
        return action;
    }

    @Override
    public ActionController getController() {
        return controller;
    }
}
