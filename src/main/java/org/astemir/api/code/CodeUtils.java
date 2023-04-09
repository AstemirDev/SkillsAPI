package org.astemir.api.code;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.StringUtils;
import org.astemir.api.client.animation.AnimationTrack;
import org.astemir.api.client.display.IDisplayArgument;
import org.astemir.api.client.model.SkillsAnimatedModel;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.objects.IAnimated;
import org.astemir.api.common.misc.ICustomRendered;

import java.util.ArrayList;
import java.util.List;

public class CodeUtils {

    public static <T extends Entity> void generateEntityType(Class<T> className,String modId){
        String name = className.getSimpleName();
        String typeName = String.join("_",StringUtils.splitByCharacterTypeCamelCase(name.replaceAll("Entity",""))).toLowerCase();
        StringBuilder builder = new StringBuilder();
        builder.append("public static final RegistryObject<EntityType<");
        builder.append(name);
        builder.append(">> ");
        builder.append(typeName.toUpperCase());
        builder.append(" = ");
        if (LivingEntity.class.isAssignableFrom(className)) {
            builder.append("RegisterUtils.register(ENTITIES,");
            builder.append(modId);
            builder.append(",");
            builder.append("\"");
            builder.append(typeName);
            builder.append("\"");
            builder.append(", new EntityProperties(");
            builder.append(name);
            builder.append("::new,MobCategory.AMBIENT,1,2,()->LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,20).add(Attributes.FOLLOW_RANGE,32).add(Attributes.ATTACK_DAMAGE,2).add(Attributes.MOVEMENT_SPEED,0.5f).build()));");
        }else{
            builder.append("ENTITIES.register(");
            builder.append("\"");
            builder.append(typeName);
            builder.append("\"");
            builder.append(", ()->EntityType.Builder.");
            builder.append("<");
            builder.append(name);
            builder.append(">");
            builder.append("of(");
            builder.append(name);
            builder.append("::new, MobCategory.MISC).sized(1,1).build(");
            builder.append("\"");
            builder.append(typeName);
            builder.append("\"");
            builder.append("));");
        }
        System.out.println(builder.toString());
    }

    public static <T extends ICustomRendered & IAnimated,K extends IDisplayArgument> void generateAnimations(SkillsAnimatedModel<T,K> model){
        List<String> animations = new ArrayList<>();
        for (AnimationTrack animationTrack : model.animations) {
            StringBuilder builder = new StringBuilder();
            String animName = "ANIMATION_"+StringUtils.substringAfterLast(animationTrack.getName(),".").toUpperCase();
            builder.append("public static Animation ");
            builder.append(animName);
            builder.append(" = ");
            builder.append("new Animation(");
            builder.append("\"");
            builder.append(animationTrack.getName());
            builder.append("\"");
            builder.append(",");
            builder.append(animationTrack.getLength()+"f");
            builder.append(")");
            if (animationTrack.getLoop() == Animation.Loop.TRUE){
                builder.append(".loop()");
            }
            if (animationTrack.getLoop() == Animation.Loop.HOLD_ON_LAST_FRAME){
                builder.append(".loop(Animation.Loop.HOLD_ON_LAST_FRAME)");
            }
            builder.append(".smoothness(1)");
            builder.append(";");
            animations.add(animName);
            System.out.println(builder.toString());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("public AnimationFactory animationFactory = new AnimationFactory(this,");
        for (int i = 0; i < animations.size(); i++) {
            String animation = animations.get(i);
            builder.append(animation);
            if (i < animations.size()-1){
                builder.append(",");
            }
        }
        builder.append(");");
        System.out.println(builder.toString());
    }
}
