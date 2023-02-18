package org.astemir.api.common.animation;



public class Animation {


    private String name;
    private int uniqueId = 0;
    private float length = 0;
    private Loop loop = Loop.FALSE;
    private int priority = 0;
    private int layer = 0;
    private float speed = 1;
    private float smoothness = 2;


    public Animation(String name, float length) {
        this.name = name;
        this.length = length;
    }

    public Animation priority(int priority){
        this.priority = priority;
        return this;
    }

    public Animation layer(int layer){
        this.layer = layer;
        return this;
    }

    public Animation smoothness(float smoothness){
        this.smoothness = smoothness;
        return this;
    }

    public Animation speed(float speed){
        this.speed = speed;
        return this;
    }

    public Animation loop(Loop loop){
        this.loop = loop;
        return this;
    }

    public Animation loop(){
        this.loop = Loop.TRUE;
        return this;
    }

    public int getLayer() {
        return layer;
    }

    public void setUniqueId(int id) {
        this.uniqueId = id;
    }

    public String getName() {
        return name;
    }

    public float getSpeed() {
        return speed;
    }

    public float getLength() {
        return length;
    }

    public Loop getLoop() {
        return loop;
    }

    public int getPriority() {
        return priority;
    }

    public float getSmoothness() {
        return smoothness;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void onTick(AnimationFactory factory, int tick){};

    public void onEnd(AnimationFactory factory){};

    public void onStart(AnimationFactory factory){};


    public enum Loop{
        FALSE,TRUE,HOLD_ON_LAST_FRAME;

        public static Loop parse(String text){
            return Loop.valueOf(text.toUpperCase());
        }
    }
}
