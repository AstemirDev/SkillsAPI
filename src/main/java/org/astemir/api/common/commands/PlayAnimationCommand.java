package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.common.animation.Animation;
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
        CommandPart info = new CommandPart("info");

        CommandBuilder builder = new CommandBuilder("playanimation")
                .variants(
                    new CommandVariant(targets,info).execute((p)->{
                        for (Entity entity : targets.getEntities(p)) {
                            if (entity instanceof IAnimatedEntity animated){
                                p.getSource().sendSuccess(formComponent(animated),true);
                            }
                        }
                        return 0;
                    }),
                    new CommandVariant(pos,info).execute((p)->{
                        BlockEntity blockEntity = p.getSource().getLevel().getBlockEntity(pos.getBlockPos(p));
                        if (blockEntity instanceof IAnimatedEntity animated){
                            p.getSource().sendSuccess(formComponent(animated),true);
                        }
                        return 0;
                    }),
                    new CommandVariant(targets,name).execute((p)->{
                     playAnimation(p.getSource(),targets.getEntities(p),name.getString(p));
                     return 0;
                    }),

                    new CommandVariant(pos,name).execute((p)->{
                     playAnimation(p.getSource(),pos.getBlockPos(p),name.getString(p));
                     return 0;
                    })
                );
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }


    private static Component formComponent(IAnimatedEntity animated){
        StringBuilder builder = new StringBuilder();
        int animationsCount = animated.getAnimationFactory().getAnimationList().getAnimations().size();
        Animation[] animations = animated.getAnimationFactory().getAnimationList().getAnimations().toArray(new Animation[animationsCount]);
        builder.append("\n");
        builder.append("§lAnimations:§r");
        builder.append("\n");
        for (int i = 0; i < animationsCount; i++) {
            Animation animation = animations[i];
            builder.append("§d");
            builder.append(animation.getName());
            builder.append("§r§f\n[");
            builder.append("§3Length: §b");
            builder.append(animation.getLength());
            builder.append("§f; ");
            builder.append("§eSpeed: §b");
            builder.append(animation.getSpeed());
            builder.append("§f; ");
            builder.append("§aLoop: §b");
            builder.append(animation.getLoop());
            builder.append("§f; ");
            builder.append("§cLayer: §b");
            builder.append(animation.getLayer());
            builder.append("§f; ");
            builder.append("§6Prior: §b");
            builder.append(animation.getPriority());
            builder.append("§f]");
            if (i != animationsCount-1){
                builder.append("§f\n ");
            }
        }
        return Component.literal(builder.toString());
    }

    public static int playAnimation(CommandSourceStack source, BlockPos pos, String animation){
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity instanceof IAnimatedEntity){
            playAnimation(((IAnimatedEntity) blockEntity),animation);
        }
        return 1;
    }

    public static int playAnimation(CommandSourceStack source, Collection<? extends Entity> entities, String animation){
        for (Entity entity : entities) {
            if (entity instanceof IAnimatedEntity){
                playAnimation((IAnimatedEntity) entity,animation);
            }
        }
        return 1;
    }

    public static void playAnimation(IAnimatedEntity animated, String animationName){
        animated.getAnimationFactory().play(animated.getAnimationFactory().getAnimationList().getAnimation(animationName));
    }

}