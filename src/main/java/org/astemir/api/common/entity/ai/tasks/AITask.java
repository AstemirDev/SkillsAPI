package org.astemir.api.common.entity.ai.tasks;


import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.common.entity.ai.AIHelper;
import org.astemir.api.common.entity.ai.AITaskSystem;
import org.astemir.api.common.entity.ai.ICustomAIEntity;
import org.astemir.api.common.entity.ai.triggers.TaskExecution;
import org.astemir.api.common.entity.ai.triggers.TaskMalus;
import org.astemir.api.common.entity.ai.triggers.TaskTrigger;

public class AITask {

    private int id = 0;
    private int priority = 0;
    private int ticks = 0;
    private boolean running = false;
    private AITaskSystem system;
    private TaskTrigger taskTrigger = TaskTrigger.AUTO_ENABLE;
    private TaskExecution execution = TaskExecution.INFINITE;
    private TaskMalus[] malus = new TaskMalus[]{};
    private int layer = 0;
    private int[] canInterrupt = new int[]{};
    private Goal goal;

    public AITask(int id) {
        this.id = id;
    }


    public boolean canStart(){
        if (goal != null){
            return goal.canUse();
        }
        return true;
    }

    public boolean canContinue(){
        if (goal != null){
            return goal.canContinueToUse();
        }
        return true;
    }

    public void start(){
        running = true;
        ticks = 0;
        if (goal != null){
            goal.start();
        }
        onStart();
    }

    public void update(){
        ticks++;
        if (goal != null){
            goal.tick();
        }
        onUpdate();
    }

    public void stop(){
        running = false;
        ticks = 0;
        if (goal != null){
            goal.stop();
        }
        onStop();
    }

    public AITask layer(int layer){
        this.layer = layer;
        return this;
    }

    public AITask interrupts(int... id){
        this.canInterrupt = id;
        return this;
    }

    public AITask malus(TaskMalus... malus){
        this.malus = malus;
        return this;
    }

    public AITask priority(int priority){
        this.priority = priority;
        return this;
    }

    public AITask trigger(TaskTrigger trigger){
        this.taskTrigger = trigger;
        return this;
    }

    public AITask execution(TaskExecution execution){
        this.execution = execution;
        return this;
    }


    public AITask setGoal(Goal goal){
        this.goal = goal;
        return this;
    }

    public void register(AITaskSystem system) {
        this.system = system;
        this.system.registerTask(this);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isConflictingWithOtherTask(){
        for (AITask runningTask : system.getRunningTasks()) {
            if (!isCanInterrupt(runningTask.getId()) && runningTask.getId() != getId()) {
                return getLayer() == runningTask.getLayer();
            }
        }
        return false;
    }

    public boolean isCanInterrupt(int id){
        for (int interruptId : getCanInterrupt()) {
            if (interruptId == id){
                return true;
            }
        }
        return false;
    }

    public int getPriority() {
        return priority;
    }

    public int getTicks() {
        return ticks;
    }

    public int getId() {
        return id;
    }

    public int getLayer() {
        return layer;
    }

    public int[] getCanInterrupt() {
        return canInterrupt;
    }

    public boolean hasMalus(TaskMalus malus){
        for (TaskMalus taskMalus : getMalus()) {
            if (taskMalus == malus){
                return true;
            }
        }
        return false;
    }

    public TaskMalus[] getMalus() {
        return malus;
    }

    public <T extends Mob & ICustomAIEntity> T getEntity(){
        return getTaskSystem().getEntity();
    }

    public TaskExecution getExecution() {
        return execution;
    }

    public TaskTrigger getTaskTrigger() {
        return taskTrigger;
    }

    public AITaskSystem getTaskSystem() {
        return system;
    }

    public AIHelper ai(){
        return getTaskSystem().getAIHelper();
    }


    public boolean onInteract(Player player, InteractionHand hand, ItemStack itemStack, Direction face){return true;}

    public boolean onHurt(DamageSource source, float damage){return true;}

    public void onStart(){}

    public void onUpdate(){}

    public void onStop(){}
}
