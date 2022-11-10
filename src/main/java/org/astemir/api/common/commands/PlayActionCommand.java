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
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.IActionListener;

import java.util.Collection;


public class PlayActionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playaction").requires((p)->p.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("controller", StringArgumentType.word()).then(Commands.argument("action", StringArgumentType.word()).executes((p)->
                        playAction(p.getSource(),BlockPosArgument.getLoadedBlockPos(p,"pos"),StringArgumentType.getString(p,"controller"),StringArgumentType.getString(p,"action"),0)).then(Commands.argument("delay",IntegerArgumentType.integer(0)).executes((p)->
                        playAction(p.getSource(),BlockPosArgument.getLoadedBlockPos(p,"pos"),StringArgumentType.getString(p,"controller"),StringArgumentType.getString(p,"action"),IntegerArgumentType.getInteger(p,"delay")))))))

                .then(Commands.argument("targets",EntityArgument.entities()).then(Commands.argument("controller", StringArgumentType.word()).then(Commands.argument("action", StringArgumentType.word()).executes((p)->
                        playAction(p.getSource(),EntityArgument.getEntities(p,"targets"),StringArgumentType.getString(p,"controller"),StringArgumentType.getString(p,"action"),0)).then(Commands.argument("delay",IntegerArgumentType.integer(0)).executes((p)->
                        playAction(p.getSource(),EntityArgument.getEntities(p,"targets"),StringArgumentType.getString(p,"controller"),StringArgumentType.getString(p,"action"),IntegerArgumentType.getInteger(p,"delay")))))))
        );
    }


    public static int playAction(CommandSourceStack source, BlockPos pos, String controllerName, String actionName, int delay){
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity instanceof IActionListener){
            playAction(((IActionListener) blockEntity),controllerName,actionName,delay);
        }
        return 1;
    }

    public static int playAction(CommandSourceStack source, Collection<? extends Entity> entities, String controllerName,String actionName,int delay){
        for (Entity entity : entities) {
            if (entity instanceof IActionListener){
                playAction((IActionListener) entity,controllerName,actionName,delay);
            }
        }
        return 1;
    }

    public static void playAction(IActionListener listener,String controllerName,String actionName,int delay){
        for (ActionController controller : listener.getActionStateMachine().getControllers()) {
            if (controller.getName().equals(controllerName)){
                for (Action action : controller.getActions()) {
                    if (actionName.equals("noAction")){
                        controller.setNoState();
                    }
                    if (action.getName().equals(actionName)){
                        controller.playAction(action,delay);
                    }
                }
            }
        }
    }

}