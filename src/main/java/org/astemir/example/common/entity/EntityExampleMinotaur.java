package org.astemir.example.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.entity.SAEntityMonster;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.network.PacketArgument;
import org.astemir.api.utils.RandomUtils;

import static org.astemir.api.utils.EntityUtils.isMoving;


public class EntityExampleMinotaur extends SAEntityMonster implements IAnimated, ISARendered {

    public static Animation ANIMATION_IDLE = new Animation("animation.model.idle",2.4f).loop().layer(0).smoothness(4);
    public static Animation ANIMATION_WALK = new Animation("animation.model.walk",2.08f).walkAnimation().layer(0).smoothness(4);
    public static Animation ANIMATION_RUN = new Animation("animation.model.run1",0.64f).walkAnimation().layer(0).smoothness(4);

    public static Animation ANIMATION_ATTACK = new Animation("animation.model.attack1",0.72f).priority(1).layer(1);
    public static Animation ANIMATION_ATTACK_2 = new Animation("animation.model.attack2",0.8f).priority(1).layer(1);
    public static Animation ANIMATION_ATTACK_3 = new Animation("animation.model.attack3",0.92f).priority(1).layer(1);

    public static Animation ANIMATION_FURY = new Animation("animation.model.fury",3.68f).priority(1).layer(1);
    public static Animation ANIMATION_START_1 = new Animation("animation.model.start1",1.52f).priority(1).layer(1);
    public static Animation ANIMATION_START_2 = new Animation("animation.model.start2",1.08f).priority(1).layer(1);


    public AnimationFactory animationFactory = new AnimationFactory(this,new AnimationList(ANIMATION_IDLE,ANIMATION_WALK,ANIMATION_RUN,ANIMATION_ATTACK,ANIMATION_ATTACK_2,ANIMATION_ATTACK_3,ANIMATION_FURY,ANIMATION_START_1,ANIMATION_START_2));

    public ActionController controller = new ActionController(this,"actionController",ACTION_FURY,ACTION_START_2);
    public static final Action ACTION_FURY = new Action(0,"fury",3.68f);
    public static final Action ACTION_START_2 = new Action(1,"start2",1.08f);

    public ActionController moveController = new ActionController(this,"moveController",ACTION_INFINITE);
    public static final Action ACTION_INFINITE = new Action(0,"infinite",-1);




    public EntityExampleMinotaur(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(ExampleModEntities.MINOTAUR.get(), p_34272_);
    }

    @Override
    public void tick() {
        super.tick();
        if (controller.is(ACTION_FURY)){
            animationFactory.play(ANIMATION_FURY);
        }
        if (controller.is(ACTION_START_2)){
            animationFactory.play(ANIMATION_START_2);
        }
        if (isMoving(this,-0.05f,0.05f)) {
            animationFactory.play(ANIMATION_WALK);
        } else {
            if (!animationFactory.isPlaying(ANIMATION_WALK)) {
                animationFactory.play(ANIMATION_IDLE);
            }
        }
    }

    @Override
    public boolean canBeLeashed(Player p_21418_) {
        return !this.isLeashed();
    }

    @Override
    public boolean doHurtTarget(Entity p_34276_) {
        if (!animationFactory.isPlayingLayer(ANIMATION_ATTACK)) {
            if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ANIMATION_ATTACK);
            } else if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ANIMATION_ATTACK_2);
            } else if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ANIMATION_ATTACK_3);
            }
        }
        return false;
    }


    @Override
    public void onActionEnd(ActionController controller, Action state) {
        if (state == ACTION_FURY){
            controller.playAction(ACTION_START_2);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == ANIMATION_ATTACK || animation == ANIMATION_ATTACK_2 || animation == ANIMATION_ATTACK_3) {
            if (getTarget() != null && getTarget().distanceTo(this) < 5) {
                getTarget().hurt(DamageSource.mobAttack(this), 5);
            }
        }
    }

    @Override
    public void onHandleClientEvent(int event, PacketArgument[] arguments) {
    }

    @Override
    public void onHandleServerEvent(int event, PacketArgument[] arguments) {

    }

    @Override
    public AnimationFactory getAnimationFactory() {
        return animationFactory;
    }
}
