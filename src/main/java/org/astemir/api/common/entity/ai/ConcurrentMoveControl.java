package org.astemir.api.common.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class ConcurrentMoveControl extends MoveControl {

    private ConcurrentMoveTask moveTask;


    public ConcurrentMoveControl(Mob p_24983_) {
        super(p_24983_);
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speedModifier) {
        if (moveTask == null) {
            super.setWantedPosition(x, y, z, speedModifier);
        } else {
            super.setWantedPosition(moveTask.getPosition().x, moveTask.getPosition().y, moveTask.getPosition().z, moveTask.getSpeed());
        }
    }

    public void setWantedPositionConcurrent(double x, double y, double z, double speedModifier) {
        this.moveTask = new ConcurrentMoveTask(new Vec3(x, y, z), speedModifier);
    }


    @Override
    public void tick() {
        super.tick();
        if (moveTask != null) {
            if (mob.position().distanceTo(moveTask.position) < 0.1f) {
                moveTask = null;
            }
        }
    }

    private class ConcurrentMoveTask {

        private Vec3 position;
        private double speed;

        public ConcurrentMoveTask(Vec3 position, double speed) {
            this.position = position;
            this.speed = speed;
        }

        public Vec3 getPosition() {
            return position;
        }

        public double getSpeed() {
            return speed;
        }
    }
}
