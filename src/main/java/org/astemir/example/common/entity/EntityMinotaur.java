package org.astemir.example.common.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.entity.SAEntityMonster;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.ActionStateMachine;
import org.astemir.api.utils.RandomUtils;

import static org.astemir.api.utils.EntityUtils.isMoving;


public class EntityMinotaur extends SAEntityMonster implements IAnimated, ITESRModel {

    public static Animation IDLE = new Animation("animation.model.idle",2.4f).loop().layer(0);
    public static Animation WALK = new Animation("animation.model.walk",2.08f).loop().layer(0);
    public static Animation RUN = new Animation("animation.model.run1",0.64f).loop().layer(0);

    public static Animation ATTACK = new Animation("animation.model.attack1",0.72f).priority(1).layer(1);
    public static Animation ATTACK_2 = new Animation("animation.model.attack2",0.8f).priority(1).layer(1);
    public static Animation ATTACK_3 = new Animation("animation.model.attack3",0.92f).priority(1).layer(1);

    public static Animation FURY = new Animation("animation.model.fury",3.68f).priority(1).layer(1);
    public static Animation START_1 = new Animation("animation.model.start1",1.52f).priority(1).layer(1);
    public static Animation START_2 = new Animation("animation.model.start2",1.08f).priority(1).layer(1);


    public AnimationFactory animationFactory = new AnimationFactory(this,new AnimationList(IDLE,WALK,RUN,ATTACK,ATTACK_2,ATTACK_3,FURY,START_1,START_2));

    public ActionController controller = new ActionController(this,"testController",TEST_ACTION);
    public static final Action TEST_ACTION = new Action(0,"testAction",20);

    public EntityMinotaur(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(ModEntities.MINOTAUR.get(), p_34272_);
    }

    @Override
    public void tick() {
        super.tick();
        if (isMoving(this,-0.15f,0.15f)) {
            animationFactory.play(WALK);
        } else {
            animationFactory.play(IDLE);
        }
    }


    @Override
    public boolean doHurtTarget(Entity p_34276_) {
        if (!animationFactory.isPlayingLayer(ATTACK)) {
            if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ATTACK);
            } else if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ATTACK_2);
            } else if (RandomUtils.doWithChance(25)) {
                animationFactory.play(ATTACK_3);
            }
        }
        return false;
    }



    @Override
    public void onAnimationTick(Animation animation, int tick) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == ATTACK || animation == ATTACK_2 || animation == ATTACK_3) {
            if (getTarget() != null && getTarget().distanceTo(this) < 5) {
                getTarget().hurt(DamageSource.mobAttack(this), 5);
            }
        }
    }


    @Override
    public AnimationFactory getAnimationFactory() {
        return animationFactory;
    }
}
