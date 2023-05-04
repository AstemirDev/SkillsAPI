package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.StringUtils;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.objects.IAnimated;
import org.astemir.api.common.animation.objects.IAnimatedBlock;
import org.astemir.api.common.animation.objects.IAnimatedEntity;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandPart;
import org.astemir.api.common.commands.build.CommandVariant;

import java.util.Collection;


public class PlayAnimationCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandArgument targets = CommandArgument.entities("targets");
        CommandArgument pos = CommandArgument.blockPos("pos");
        CommandArgument name = CommandArgument.word("name");

        CommandBuilder builder = new CommandBuilder("animation")
                .variants(
                    new CommandVariant(CommandPart.create("play"),targets,name).execute((p)->{
                        playAnimation(p.getSource(),targets.getEntities(p),name.getString(p));
                        return 0;
                    }),
                    new CommandVariant(CommandPart.create("play"),pos,name).execute((p)->{
                        playAnimation(p.getSource(),pos.getBlockPos(p),name.getString(p));
                        return 0;
                    }),
                    new CommandVariant(CommandPart.create("stop"),targets,name).execute((p)->{
                        stopAnimation(p.getSource(),targets.getEntities(p),name.getString(p));
                        return 0;
                    }),
                    new CommandVariant(CommandPart.create("stop"),pos,name).execute((p)->{
                        stopAnimation(p.getSource(),pos.getBlockPos(p),name.getString(p));
                        return 0;
                    }),
                    new CommandVariant(CommandPart.create("info"),targets).execute((p)->{
                        for (Entity entity : targets.getEntities(p)) {
                            if (entity instanceof IAnimatedEntity animated){
                                p.getSource().sendSuccess(formComponent(animated),true);
                            }
                        }
                        return 0;
                    }),
                    new CommandVariant(CommandPart.create("info"),pos).execute((p)->{
                        BlockEntity blockEntity = p.getSource().getLevel().getBlockEntity(pos.getBlockPos(p));
                        if (blockEntity instanceof IAnimatedBlock animated){
                            p.getSource().sendSuccess(formComponent(animated),true);
                        }
                        return 0;
                    })
                );
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }


    private static Component formComponent(IAnimated animated){
        int animationsCount = animated.getAnimationFactory().getAnimationList().getAnimations().size();
        Animation[] animations = animated.getAnimationFactory().getAnimationList().getAnimations().toArray(new Animation[animationsCount]);
        MutableComponent result = Component.literal("§lAnimations§r: ");
        for (int i = 0; i < animationsCount; i++) {
            String id = (animated instanceof Entity entity) ? String.valueOf(entity.getUUID()) : (animated instanceof BlockEntity blockEntity) ? blockEntity.getBlockPos().getX()+" "+blockEntity.getBlockPos().getY()+" "+blockEntity.getBlockPos().getZ() : "null";
            Animation animation = animations[i];
            String normalizedName = StringUtils.capitalize(animation.getName().replace("animation.model.",""));
            Component mutableComponent = Component.literal(
                    String.format("Name: %s\nLength: %s\nLoop: %s\nPriority: %s\nLayer: %s\nSmoothness: %s\nSpeed: %s",
                            animation.getName(),
                            animation.getLength(),
                            animation.getLoop(),
                            animation.getPriority(),
                            animation.getLayer(),
                            animation.getSmoothness(),
                            animation.getSpeed()));
            MutableComponent mutableComponent2 = Component.literal(String.format("%s",normalizedName)).
                    withStyle(Style.EMPTY.
                            withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,mutableComponent)).
                            withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/animation play %s %s".formatted(id,animation.getName())))
                    );
            if (i != animationsCount - 1) {
                mutableComponent2.append(", ");
            }
            result = result.append(mutableComponent2);
        }
        return result;
    }

    public static int playAnimation(CommandSourceStack source, BlockPos pos, String animation){
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity instanceof IAnimatedBlock animatedBlock){
            playAnimation(animatedBlock,animation);
        }
        return 1;
    }

    public static int playAnimation(CommandSourceStack source, Collection<? extends Entity> entities, String animation){
        for (Entity entity : entities) {
            if (entity instanceof IAnimatedEntity animatedEntity){
                playAnimation(animatedEntity,animation);
            }
        }
        return 1;
    }

    public static void playAnimation(IAnimated animated, String animationName){
        animated.getAnimationFactory().play(animated.getAnimationFactory().getAnimationList().getAnimation(animationName));
    }



    public static int stopAnimation(CommandSourceStack source, BlockPos pos, String animation){
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity instanceof IAnimatedBlock animatedBlock){
            stopAnimation(animatedBlock,animation);
        }
        return 1;
    }

    public static int stopAnimation(CommandSourceStack source, Collection<? extends Entity> entities, String animation){
        for (Entity entity : entities) {
            if (entity instanceof IAnimatedEntity animatedEntity){
                stopAnimation(animatedEntity,animation);
            }
        }
        return 1;
    }

    public static void stopAnimation(IAnimated animated, String animationName){
        animated.getAnimationFactory().stop(animated.getAnimationFactory().getAnimationList().getAnimation(animationName));
    }
}