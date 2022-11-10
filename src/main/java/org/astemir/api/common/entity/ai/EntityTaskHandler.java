package org.astemir.api.common.entity.ai;

import net.minecraft.world.entity.Entity;

import java.util.concurrent.ConcurrentHashMap;

public class EntityTaskHandler {

    private ConcurrentHashMap<String, EntityTask> tasks = new ConcurrentHashMap<>();

    private Entity entity;

    public EntityTaskHandler(Entity entity) {
        this.entity = entity;
    }

    public void update(){
        for (EntityTask task : tasks.values()) {
            task.update();
        }
    }

    public void stop(String taskName){
        tasks.get(taskName).stop();
    }

    public void stopAll(){
        tasks.forEach((name,task)->{
            task.stop();
        });
    }

    public EntityTask runRepeatingTask(String taskName, Runnable runnable, int delay, boolean removeOnStop){
        if (!tasks.containsKey(taskName)) {
            return tasks.put(taskName,new EntityTask(entity,delay) {
                @Override
                public void run() {
                    runnable.run();
                }
                @Override
                public void end() {
                    if (removeOnStop) {
                        tasks.remove(taskName);
                    }
                }
            });
        }else {
            return tasks.get(taskName);
        }
    }

    public EntityTask runRepeatingTask(String taskName, Runnable runnable, int delay){
        return runRepeatingTask(taskName,runnable,delay,false);
    }

    public EntityTask runDelayedTask(String taskName, Runnable runnable, int delay){
        return runRepeatingTask(taskName,runnable,delay,true).shouldStopAfterRun();
    }
}
