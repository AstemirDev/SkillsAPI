package org.astemir.api.common.action;



public class Action {

    private int id = 0;
    private String name;
    private int length = 0;
    private boolean canOverrideSelf = false;
    private ActionLinks links = new ActionLinks();

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

    public Action onStart(ActionLinks.Start start){
        links.onStart = start;
        return this;
    }

    public Action onTick(ActionLinks.Tick tick){
        links.onTick = tick;
        return this;
    }

    public Action onEnd(ActionLinks.End end){
        links.onEnd = end;
        return this;
    }

    public ActionLinks getLinks() {
        return links;
    }

    @Override
    public String toString() {
        StringBuilder builder = new  StringBuilder();
        builder = builder.append("[").append(name).append(" ").append(id).append("]");
        return builder.toString();
    }

    public void onStart(ActionController controller){}

    public void onTick(ActionController controller,int tick){}

    public void onEnd(ActionController controller){}
}
