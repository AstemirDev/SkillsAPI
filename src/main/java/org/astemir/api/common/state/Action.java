package org.astemir.api.common.state;


import org.astemir.api.common.animation.Animation;

public class Action {

    private int id = 0;
    private String name;
    private int length = 0;
    private boolean canOverrideSelf = false;
    private Animation animation;

    public Action(int id,String name, int length) {
        this.id = id;
        this.name = name;
        this.length = length;
    }

    public Action(int id,String name, float length) {
        double time = length * 20;
        int ticks = (int)time;
        this.id = id;
        this.name = name;
        this.length = ticks;
    }

    public Action(int id,String name,Animation animation) {
        this(id,name,animation.getLength());
        this.animation = animation;
    }

    public Action canOverrideSelf() {
        canOverrideSelf = true;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public boolean isCanOverrideSelf() {
        return canOverrideSelf;
    }

    public Animation getAnimation() {
        return animation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new  StringBuilder();
        builder = builder.append("[").append(name).append(" ").append(id).append("]");
        return builder.toString();
    }
}
