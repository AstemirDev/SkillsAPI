package org.astemir.api.client.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.astemir.api.math.vector.Vector2;
import org.astemir.api.math.vector.Vector3;

@Cancelable
public class GlobalCameraSetupEvent extends Event {


    private Entity entity;
    private float partialTick;
    boolean detached;
    boolean thirdPersonReverse;
    private Vector2 rotation = new Vector2(0,0);
    private Vector3 position = new Vector3(0,0,0);
    private Vector3 offset = new Vector3(0,0,0);
    private boolean vanillaBehavior = true;

    public GlobalCameraSetupEvent(Entity entity, float partialTick, boolean detached, boolean thirdPersonReverse) {
        this.entity = entity;
        this.partialTick = partialTick;
        this.detached = detached;
        this.thirdPersonReverse = thirdPersonReverse;
    }

    public void setRotation(float x,float y){
        this.rotation = new Vector2(x,y);
    }

    public void setPosition(double x,double y,double z){
        this.position = new Vector3(x,y,z);
    }

    public void offset(double x, double y, double z){
        this.offset = new Vector3(x,y,z);
    }


    public Entity getEntity() {
        return entity;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public boolean isDetached() {
        return detached;
    }

    public boolean isThirdPersonReverse() {
        return thirdPersonReverse;
    }

    public Vector2 getRotation() {
        return rotation;
    }

    public void setRotation(Vector2 rotation) {
        this.rotation = rotation;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getOffset() {
        return offset;
    }

    public void disableVanillaBehavior(){
        vanillaBehavior = false;
    }

    public boolean isVanillaBehavior() {
        return vanillaBehavior;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
