package org.astemir.example.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.common.entity.ISkillsMob;
import org.astemir.api.common.action.ActionStateMachine;
import org.astemir.api.common.animation.*;
import org.astemir.api.common.action.Action;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.entity.ai.SmartPathNavigateGround;
import org.astemir.api.common.entity.ai.AITaskSystem;
import org.astemir.api.common.entity.ai.ICustomAIEntity;
import org.astemir.api.common.entity.ai.tasks.AITaskAttack;

import static org.astemir.api.common.entity.utils.EntityUtils.isMoving;


public class EntityExampleMinotaur extends Monster implements ISkillsMob, ICustomAIEntity {

    public AnimationFactory animationFactory = new AnimationFactory(this,ANIMATION_IDLE,ANIMATION_WALK,ANIMATION_RUN,ANIMATION_ATTACK,ANIMATION_ATTACK_2,ANIMATION_ATTACK_3,ANIMATION_FURY,ANIMATION_START_1,ANIMATION_START_2);
    public static Animation ANIMATION_IDLE = new Animation("animation.model.idle",2.4f).loop().layer(0).smoothness(4);
    public static Animation ANIMATION_WALK = new Animation("animation.model.walk",2.08f).loop().layer(0).smoothness(4);
    public static Animation ANIMATION_RUN = new Animation("animation.model.run1",0.64f).loop().layer(0).smoothness(4);
    public static Animation ANIMATION_ATTACK_2 = new Animation("animation.model.attack2",0.8f).priority(1).layer(1);
    public static Animation ANIMATION_ATTACK_3 = new Animation("animation.model.attack3",0.92f).priority(1).layer(1);
    public static Animation ANIMATION_FURY = new Animation("animation.model.fury",3.68f).priority(1).layer(1);
    public static Animation ANIMATION_START_1 = new Animation("animation.model.start1",1.52f).priority(1).layer(1);
    public static Animation ANIMATION_START_2 = new Animation("animation.model.start2",1.08f).priority(1).layer(1);
    public static Animation ANIMATION_ATTACK = new Animation("animation.model.attack1",0.72f).priority(1).layer(1).onTick((factory,tick)->{
        if (tick == 10) {
            EntityExampleMinotaur minotaur = factory.getAnimated();
            AITaskAttack taskAttack = minotaur.getAISystem().getRunningTaskById(1);
            if (taskAttack != null) {
                taskAttack.ai().attack(taskAttack,minotaur.getTarget(),3);
            }
        }
    });

    public ActionController controller = ActionController.create(this,"actionController",ACTION_FURY,ACTION_START_2);
    public static final Action ACTION_START_2 = new Action(1,"start2",1.08f);
    public static final Action ACTION_FURY = new Action(0,"fury",3.68f).onEnd((controller)->{
        controller.playAction(controller.getActionByName("start2"));
    });

    public ActionController moveController = ActionController.create(this,"moveController",ACTION_INFINITE);
    public static final Action ACTION_INFINITE = new Action(0,"infinite",-1);

    public ActionStateMachine stateMachine = ActionStateMachine.loadControllers(controller,moveController);


    private AITaskSystem aiTaskSystem;


    public EntityExampleMinotaur(EntityType<? extends Monster> p_34271_, Level p_34272_) {
        super(ExampleModEntities.MINOTAUR.get(), p_34272_);
    }

    @Override
    public void setupAI() {
        this.aiTaskSystem = new AITaskSystem(this);
        CustomMinotaurAI.wanderingTask(this);
        CustomMinotaurAI.attackTask(this);
        CustomMinotaurAI.appleEatingHappiness(this);
        CustomMinotaurAI.targetFind(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new SmartPathNavigateGround(this,pLevel);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this);
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
            animationFactory.play(ANIMATION_IDLE);
        }
    }

    @Override
    public boolean canBeLeashed(Player p_21418_) {
        return !this.isLeashed();
    }

    @Override
    public <K extends IDisplayArgument> AnimationFactory getAnimationFactory(K argument) {
        return animationFactory;
    }

    @Override
    public ActionStateMachine getActionStateMachine() {
        return stateMachine;
    }

    @Override
    public AITaskSystem getAISystem() {
        return aiTaskSystem;
    }
}
