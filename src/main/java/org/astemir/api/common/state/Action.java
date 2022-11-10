package org.astemir.api.common.state;



public class Action {

    private int id = 0;
    private String name;
    private int length = 0;
    private boolean canOverrideSelf = false;

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

    @Override
    public String toString() {
        StringBuilder builder = new  StringBuilder();
        builder = builder.append("[").append(name).append(" ").append(id).append("]");
        return builder.toString();
    }
}
