package org.astemir.api.common.entity.ai.tasks;


import org.astemir.api.common.entity.ai.triggers.TaskExecution;

public class AITaskTimer extends AITask {

    private int delay = 0;
    private int time = 0;

    public AITaskTimer(int id, int delay) {
        super(id);
        this.delay = delay;
    }

    public void onRun(){}

    @Override
    public void update() {
        super.update();
        if (time < delay){
            time++;
        }else{
            time = 0;
            onRun();
            if (getExecution() == TaskExecution.ONCE){
                stop();
            }
        }
    }
}
