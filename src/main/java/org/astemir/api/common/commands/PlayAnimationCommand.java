package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.common.animation.IAnimated;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.IActionListener;

import java.util.Collection;


public class PlayAnimationCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playanimation").requires((p)->p.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("name", StringArgumentType.word()).executes((p)->
                        playAnimation(p.getSource(),BlockPosArgument.getLoadedBlockPos(p,"pos"),StringArgumentType.getString(p,"name")))))

                .then(Commands.argument("targets",EntityArgument.entities()).then(Commands.argument("name", StringArgumentType.word()).executes((p)->
                        playAnimation(p.getSource(),EntityArgument.getEntities(p,"targets"),StringArgumentType.getString(p,"name")))))
        );
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