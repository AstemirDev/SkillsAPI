package org.astemir.api.common;

public class Cooldown {

    private int delay;
    private int ticks;
    private boolean canBeUsed = true;

    public Cooldown(int delay){
        this.delay = delay;
    }


    public void update(){
        if (!canBeUsed) {
            if (ticks < delay) {
                ticks++;
            } else if (ticks == delay) {
                ticks = 0;
                canBeUsed = true;
            }
        }
    }

    public int getDelay() {
        return delay;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean canUse() {
        return canBeUsed;
    }

    public void cooldown(){
        canBeUsed = false;
    }
}
