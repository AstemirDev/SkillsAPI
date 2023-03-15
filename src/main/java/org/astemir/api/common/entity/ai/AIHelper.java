package org.astemir.api.common.entity.ai;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.astemir.api.common.action.Action;
import org.astemir.api.common.entity.ai.tasks.AITask;
import org.astemir.api.common.entity.ai.tasks.IActionTask;
import org.astemir.api.common.entity.ai.triggers.TaskMalus;

public class AIHelper {

    private AITaskSystem aiTaskSystem;

    public AIHelper(AITaskSystem aiTaskSystem) {
        this.aiTaskSystem = aiTaskSystem;
    }

    public boolean navigatorMoveEntity(AITask request, double x, double y, double z, float speed){
        if (!isConflictsAt(request, TaskMalus.MOVE)) {
            aiTaskSystem.getEntity().getNavigation().moveTo(x, y, z, speed);
            return true;
        }
        return false;
    }

    public boolean navigatorMoveEntity(AITask request, Entity entity, float speed){
        if (!isConflictsAt(request,TaskMalus.MOVE)) {
            aiTaskSystem.getEntity().getNavigation().moveTo(entity, speed);
            return true;
        }
        return false;
    }

    public boolean moveEntity(AITask request,Entity entity,float speed){
        if (!isConflictsAt(request,TaskMalus.MOVE)) {
            aiTaskSystem.getEntity().getMoveControl().setWantedPosition(entity.getX(), entity.getY(), entity.getZ(), speed);
            return true;
        }
        return false;
    }

    public boolean moveEntity(AITask request,double x, double y, double z, float speed){
        if (!isConflictsAt(request,TaskMalus.MOVE)) {
            aiTaskSystem.getEntity().getMoveControl().setWantedPosition(x, y, z, speed);
            return true;
        }
        return false;
    }

    public boolean lookEntity(AITask request,double x, double y, double z, float xSpeed,float ySpeed){
        if (!isConflictsAt(request,TaskMalus.LOOK)) {
            aiTaskSystem.getEntity().getLookControl().setLookAt(x, y, z, xSpeed, ySpeed);
            return true;
        }
        return false;
    }

    public boolean lookEntity(AITask request,Entity entity, float xSpeed,float ySpeed){
        if (!isConflictsAt(request,TaskMalus.LOOK)) {
            aiTaskSystem.getEntity().getLookControl().setLookAt(entity, xSpeed, ySpeed);
            return true;
        }
        return false;
    }

    public boolean attack(AITask request, LivingEntity target,float distance){
        if (!isConflictsAt(request,TaskMalus.ATTACK)){
            Mob mob = aiTaskSystem.getEntity();
            if (mob != null && target != null && !mob.isRemoved() && !target.isRemoved() && mob.distanceTo(target) <= distance) {
                mob.swing(InteractionHand.MAIN_HAND);
                mob.doHurtTarget(target);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean jump(AITask request){
        if (!isConflictsAt(request,TaskMalus.JUMP)){
            aiTaskSystem.getEntity().getJumpControl().jump();
            return true;
        }
        return false;
    }



    public boolean playAction(AITask request, Action action, int time){
        if (!isConflictsAt(request,TaskMalus.ACTION)){
            if (request instanceof IActionTask actionTask){
                actionTask.getController().playAction(action,time);
                return true;
            }
        }
        return false;
    }

    public boolean playAction(AITask request,Action action){
        return playAction(request,action,0);
    }

    public boolean isConflictsAt(AITask task,TaskMalus malus){
        for (AITask runningTask : aiTaskSystem.getRunningTasks()) {
            if (runningTask.getId() != task.getId()){
                if (malus == TaskMalus.ACTION){
                    if (task instanceof IActionTask actionTask1 && runningTask instanceof IActionTask actionTask2){
                        if (runningTask.hasMalus(malus)){
                            return actionTask1.getController() == actionTask2.getController();
                        }
                    }
                }
                return runningTask.hasMalus(malus);
            }
        }
        return false;
    }
}
