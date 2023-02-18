package org.astemir.api.common.entity.ai.tasks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.astemir.api.common.entity.utils.EntityUtils;
import org.astemir.api.common.entity.ai.triggers.TaskMalus;

public class AITaskAttack extends AITask{

    private float speed;
    private float attackDistance;
    private int attackDelay;
    private LivingEntity target;
    private AttackFunction attackFunction;
    private boolean isShouldSeeTarget = true;
    private int attackTicks = 0;

    public AITaskAttack(int id,float speed,float attackDistance,int attackDelay,AttackFunction attackFunction) {
        super(id);
        this.speed = speed;
        this.attackDistance = attackDistance;
        this.attackDelay = attackDelay;
        this.attackFunction = attackFunction;
        malus(TaskMalus.ATTACK,TaskMalus.MOVE,TaskMalus.MOVE);
    }

    public AITaskAttack(int id,float speed) {
        this(id,speed,2,20,null);
    }

    @Override
    public boolean canStart() {
        return getEntity().getTarget() != null;
    }

    @Override
    public boolean canContinue() {
        return target != null && !target.isRemoved();
    }

    public boolean canContinueAttack(){
        return true;
    }

    public boolean canUseAttack(){
        return attackTicks % attackDelay == 0;
    }

    @Override
    public void onStart() {
        target = getEntity().getTarget();
        attackTicks = 0;
    }

    @Override
    public void onStop() {
        attackTicks = 0;
        target = null;
        getEntity().setTarget(null);
        getEntity().getNavigation().stop();
    }

    @Override
    public void onUpdate() {
        Mob owner = getEntity();
        if (target != null && !target.isRemoved()) {
            if (!owner.canAttack(target)){
                stop();
                return;
            }
            if (target instanceof Player player){
                if (!EntityUtils.canBeTargeted(player)){
                    stop();
                    return;
                }
            }
            if (canContinueAttack()) {
                if (isShouldSeeTarget){
                    boolean canSee = owner.getSensing().hasLineOfSight(target);
                    if (!canSee){
                        stop();
                        return;
                    }
                }
                if (owner.distanceTo(target) > attackDistance) {
                    ai().lookEntity(this,target, 10, 10f);
                    if (getTicks() % 10 == 0) {
                        ai().navigatorMoveEntity(this,target.getX(),target.getY(),target.getZ(), speed);
                    }
                } else {
                    if (canUseAttack()) {
                        if (attackFunction != null) {
                            attackFunction.attack(target);
                        } else {
                            attack();
                        }
                    }
                    attackTicks++;
                }
            }
        }
    }

    public void attack(){
        ai().attack(this,target,attackDistance);
    }

    public AITaskAttack shouldSeeTarget(boolean b){
        isShouldSeeTarget = b;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public interface AttackFunction{
        void attack(LivingEntity target);
    }
}
