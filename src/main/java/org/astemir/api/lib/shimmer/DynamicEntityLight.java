package org.astemir.api.lib.shimmer;

import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.Entity;
import org.astemir.api.math.components.Color;

public class DynamicEntityLight {
    private ColorPointLight pointLight;
    private float r = 1;
    private float g = 1;
    private float b = 1;
    private float a = 1;
    private float radius = 1;

    public DynamicEntityLight(float r,float g,float b,float a, float radius) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.radius = radius;
        this.pointLight = LightManager.INSTANCE.addLight(new Vector3f(0,0,0),1,radius,false);
        this.pointLight.setColor(r,g,b,a);
    }

    public DynamicEntityLight(Color color,float radius) {
        this(color.r,color.g,color.b,color.a,radius);
    }

    public void tick(Entity entity){
        pointLight.setPos((float)entity.getX(), (float)entity.getY(), (float)entity.getZ());
        pointLight.update();
    }

    public DynamicEntityLight create(){
        if (pointLight == null || pointLight.isRemoved()){
            this.pointLight = LightManager.INSTANCE.addLight(new Vector3f(0,0,0),1,radius,false);
            this.pointLight.setColor(r,g,b,a);
        }
        return this;
    }

    public Color getColor(){
        return new Color(r,g,b,a);
    }

    public void setColor(Color color){
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.r = color.a;
        if (pointLight != null) {
            this.pointLight.setColor(r, g, b, a);
        }
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        if (pointLight != null) {
            this.pointLight.radius = radius;
        }
    }

    public void remove(){
        pointLight.remove();
    }
}
