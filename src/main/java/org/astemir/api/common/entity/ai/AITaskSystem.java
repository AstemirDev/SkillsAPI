package org.astemir.api.common.entity.ai;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.astemir.api.common.entity.ai.tasks.AITask;
import org.astemir.api.common.entity.ai.triggers.TaskTrigger;
import org.astemir.api.common.entity.utils.EntityUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class AITaskSystem {

    private List<AITask> tasks = new ArrayList<>();
    private List<Entity> sensedEntities = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<AITask> runningTasks = new CopyOnWriteArrayList<>();
    private Map<EntityType,Integer> senseForEntities = new HashMap<>();
    private Mob entity;
    private AIHelper aiHelper = new AIHelper(this);
    private float updateSensedEntitiesTime = 20;

    public AITaskSystem(Mob entity) {
        this.entity = entity;
    }

    public AITaskSystem registerTask(AITask task){
        task.register(this);
        tasks.add(task);
        tasks.sort(Comparator.comparingInt(AITask::getPriority));
        return this;
    }

    public boolean runTask(AITask task){
        if (canRunTask(task)) {
            for (int i : task.getCanInterrupt()) {
                AITask interruptedTask = getRunningTaskById(i);
                if (interruptedTask != null){
                    interruptedTask.stop();
                }
            }
            task.start();
            runningTasks.add(task);
            runningTasks.sort(Comparator.comparingInt(AITask::getPriority));
            return true;
        }
        return false;
    }


    public boolean runTask(int id){
        AITask task = getTaskById(id);
        return runTask(task);
    }


    public void stopTask(int id){
        stopTask(getRunningTaskById(id));
    }

    public void stopTask(AITask task){
        task.stop();
        runningTasks.remove(task);
        runningTasks.sort(Comparator.comparingInt(AITask::getPriority));
    }

    public void forceRunTask(AITask task){
        if (!task.isRunning()){
            for (AITask runningTask : runningTasks) {
                if (runningTask.getLayer() == task.getLayer()){
                    stopTask(runningTask);
                }
            }
            task.start();
            runningTasks.add(task);
            runningTasks.sort(Comparator.comparingInt(AITask::getPriority));
        }
    }

    public void forceRunTask(int id){
        forceRunTask(getTaskById(id));
    }

    public void update(){
        if (!entity.isNoAi()) {
            if (entity.tickCount % updateSensedEntitiesTime == 0) {
                float followRange = (float) entity.getAttributeValue(Attributes.FOLLOW_RANGE);
                sensedEntities = EntityUtils.getEntities(Entity.class, entity.level, entity.blockPosition(), followRange, (testEntity) -> senseForEntities.containsKey(testEntity.getType()));
            }
            for (AITask runningTask : runningTasks) {
                if (runningTask.isRunning()) {
                    if (runningTask.canContinue() && !runningTask.isConflictingWithOtherTask()) {
                        runningTask.update();
                    } else {
                        stopTask(runningTask);
                    }
                }else{
                    stopTask(runningTask);
                }
            }
            for (AITask task : tasks) {
                if (task.getTaskTrigger() == TaskTrigger.AUTO_ENABLE) {
                    runTask(task);
                }
            }
        }
    }

    public void handlePlayerInteraction(Player player, InteractionHand hand, ItemStack itemStack, Direction face){
        for (AITask task : tasks) {
            if (task.getTaskTrigger() == TaskTrigger.INTERACTION) {
                if (canRunTask(task)) {
                    if (task.onInteract(player, hand, itemStack, face)) {
                        runTask(task);
                    }
                }
            }
        }
    }

    public void handleHurt(DamageSource source,float damage){
        for (AITask task : tasks) {
            if (task.getTaskTrigger() == TaskTrigger.HURT) {
                if (canRunTask(task)) {
                    if (task.onHurt(source, damage)) {
                        runTask(task);
                    }
                }
            }
        }
    }

    public boolean isRunning(int id){
        return getRunningTaskById(id) != null;
    }

    public boolean canRunTask(AITask task){
        return task.canStart() && !task.isConflictingWithOtherTask() && !task.isRunning();
    }

    public <T extends AITask> T getRunningTaskById(int id){
        for (AITask runningTask : runningTasks) {
            if (runningTask.getId() == id){
                return (T)runningTask;
            }
        }
        return null;
    }

    public <T extends AITask> T getRunningTask(Class<T> className){
        for (AITask runningTask : runningTasks) {
            if (runningTask.getClass() == className){
                return (T)runningTask;
            }
        }
        return null;
    }

    public <T extends AITask> T getTaskById(int id){
        for (AITask task : tasks) {
            if (task.getId() == id){
                return (T)task;
            }
        }
        return null;
    }

    public AITaskSystem senseUpdateTime(float updateSensedEntitiesTime) {
        this.updateSensedEntitiesTime = updateSensedEntitiesTime;
        return this;
    }

    public List<Entity> getSensedEntities() {
        return sensedEntities;
    }

    public <T extends Entity> T getNearbyEntity(Class<T> entityClass, Predicate<Entity> predicate){
        T nearbyEntity = null;
        for (Entity sensedEntity : sensedEntities) {
            if (sensedEntity.getClass() == entityClass && predicate.test(sensedEntity)){
                if (nearbyEntity == null) {
                    nearbyEntity = (T) sensedEntity;
                }else{
                    if (sensedEntity.distanceTo(entity) < nearbyEntity.distanceTo(entity)){
                        nearbyEntity = (T) sensedEntity;
                    }
                }
            }
        }
        return nearbyEntity;
    }


    public List<AITask> getTasks() {
        return tasks;
    }

    public CopyOnWriteArrayList<AITask> getRunningTasks() {
        return runningTasks;
    }

    public AIHelper getAIHelper() {
        return aiHelper;
    }

    public <T extends Mob & ICustomAIEntity> T getEntity(){
        return (T) entity;
    }

}
