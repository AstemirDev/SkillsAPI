package org.astemir.api.common.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.math.Vector3;

public class ConcurrentLookControl extends LookControl {

    private ConcurrentLookTask lookTask;


    public ConcurrentLookControl(Mob p_24983_) {
        super(p_24983_);
    }

    @Override
    public void setLookAt(double x, double y, double z, float yMaxRotSpeed, float xMaxRotAngle) {
        if (lookTask == null) {
            super.setLookAt(x, y, z, yMaxRotSpeed, xMaxRotAngle);
        }
    }

    public void setLookAtConcurrent(double x, double y, double z, float yMaxRotSpeed, float xMaxRotAngle, int cooldown) {
        this.lookTask = new ConcurrentLookTask(new Vec3(x, y, z), yMaxRotSpeed, xMaxRotAngle, cooldown);
        super.setLookAt(lookTask.getPosition().x, lookTask.getPosition().y, lookTask.getPosition().z, lookTask.getyMaxRotSpeed(), lookTask.getxMaxRotAngle());
        lookAtCooldown = lookTask.getCooldown();
    }

    public void setLookAtConcurrent(double x, double y, double z, int cooldown) {
        this.lookTask = new ConcurrentLookTask(new Vec3(x, y, z), yMaxRotSpeed, xMaxRotAngle, cooldown);
        super.setLookAt(lookTask.getPosition().x, lookTask.getPosition().y, lookTask.getPosition().z, (float) this.mob.getHeadRotSpeed(), (float) this.mob.getMaxHeadXRot());
        lookAtCooldown = lookTask.getCooldown();
    }

    public void setLookAtConcurrent(Entity p_148052_, int cooldown) {
        setLookAtConcurrent(p_148052_.getX(), getWantedY(p_148052_), p_148052_.getZ(), cooldown);
    }


    @Override
    public void tick() {
        if (lookTask != null) {
            Vector3 concurrentDirection = Vector3.from((float) mob.getX(), (float) mob.getEyeY(), (float) mob.getZ()).direction(Vector3.from(lookTask.position));
            Vector3 wantedDirection = Vector3.fromYawPitch(mob.yHeadRot, mob.getXRot());
            if (concurrentDirection.distanceTo(wantedDirection) < 0.1f) {
                lookTask = null;
            }
        }
        super.tick();
    }


    private class ConcurrentLookTask {

        private Vec3 position;
        private float yMaxRotSpeed;
        private float xMaxRotAngle;
        private int cooldown = 2;

        public ConcurrentLookTask(Vec3 position, float yMaxRotSpeed, float xMaxRotAngle) {
            this.position = position;
            this.yMaxRotSpeed = yMaxRotSpeed;
            this.xMaxRotAngle = xMaxRotAngle;
        }

        public ConcurrentLookTask(Vec3 position, float yMaxRotSpeed, float xMaxRotAngle, int cooldown) {
            this.position = position;
            this.yMaxRotSpeed = yMaxRotSpeed;
            this.xMaxRotAngle = xMaxRotAngle;
            this.cooldown = cooldown;
        }

        public int getCooldown() {
            return cooldown;
        }

        public Vec3 getPosition() {
            return position;
        }

        public float getyMaxRotSpeed() {
            return yMaxRotSpeed;
        }

        public float getxMaxRotAngle() {
            return xMaxRotAngle;
        }
    }

    private static double getWantedY(Entity p_24967_) {
        return p_24967_ instanceof LivingEntity ? p_24967_.getEyeY() : (p_24967_.getBoundingBox().minY + p_24967_.getBoundingBox().maxY) / 2.0D;
    }
}
