package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandPart;
import org.astemir.api.common.commands.build.CommandVariant;
import org.astemir.api.common.gfx.GFXBlackIn;
import org.astemir.api.common.gfx.GFXBlackInOut;
import org.astemir.api.common.gfx.GFXBlackOut;
import org.astemir.api.common.gfx.GFXScreenShake;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.IActionListener;
import org.astemir.api.math.Color;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


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
                            if (entity instanceof IAnimated animated){
                                p.getSource().sendSuccess(formComponent(animated),true);
                            }
                        }
                        return 0;
                    }),
                    new CommandVariant(pos,info).execute((p)->{
                        BlockEntity blockEntity = p.getSource().getLevel().getBlockEntity(pos.getBlockPos(p));
                        if (blockEntity instanceof IAnimated animated){
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


    private static Component formComponent(IAnimated animated){
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
        if (blockEntity instanceof IAnimated){
            playAnimation(((IAnimated) blockEntity),animation);
        }
        return 1;
    }

    public static int playAnimation(CommandSourceStack source, Collection<? extends Entity> entities, String animation){
        for (Entity entity : entities) {
            if (entity instanceof IAnimated){
                playAnimation((IAnimated) entity,animation);
            }
        }
        return 1;
    }

    public static void playAnimation(IAnimated animated, String animationName){
        animated.getAnimationFactory().play(animated.getAnimationFactory().getAnimationList().getAnimation(animationName));
    }

}