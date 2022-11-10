package org.astemir.api.common;

import net.minecraft.world.entity.Entity;
import org.astemir.api.SkillsAPI;
import org.astemir.api.common.entity.ai.EntityTask;

import java.util.concurrent.CopyOnWriteArrayList;

public class GlobalTaskHandler {

    private CopyOnWriteArrayList<EntityTask> tasks = new CopyOnWriteArrayList<>();



    public void runRepeatTask(Entity entity,Runnable runnable,int time,int count){
        tasks.add(new EntityTask(entity,time) {
            int i = 0;
            @Override
            public void run() {
                if (i < count){
                    runnable.run();
                    i++;
                }else{
                    stop();
                }
            }

            @Override
            public void end() {
                tasks.remove(this);
            }
        });
    }

    public void update(){
        for (EntityTask task : tasks) {
            task.update();
        }
    }

    public static GlobalTaskHandler getInstance(){
        return SkillsAPI.GLOBAL_TASK_HANDLER;
    }
}
