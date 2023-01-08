package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandPart;
import org.astemir.api.common.commands.build.CommandVariant;
import org.astemir.api.common.state.Action;
import org.astemir.api.common.state.ActionController;
import org.astemir.api.common.state.IActionListener;

import java.util.Collection;


public class PlayActionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandArgument targets = CommandArgument.entities("targets");
        CommandArgument pos = CommandArgument.blockPos("pos");
        CommandArgument controller = CommandArgument.word("controller");
        CommandArgument action = CommandArgument.word("action");
        CommandArgument delay = CommandArgument.integer("delay",0);
        CommandPart info = new CommandPart("info");

        CommandBuilder builder = new CommandBuilder("playaction")
                .variants(
                        new CommandVariant(targets,controller,action,delay).execute((p)->{
                            playAction(p.getSource(),targets.getEntities(p),controller.getString(p),action.getString(p),delay.getInt(p));
                            return 1;
                        }),new CommandVariant(pos,controller,action,delay).execute((p)->{
                            playAction(p.getSource(),pos.getBlockPos(p),controller.getString(p),action.getString(p),delay.getInt(p));
                            return 1;
                        }),
                        new CommandVariant(targets,controller,action).execute((p)->{
                            playAction(p.getSource(),targets.getEntities(p),controller.getString(p),action.getString(p),0);
                            return 1;
                        }),new CommandVariant(pos,controller,action).execute((p)->{
                            playAction(p.getSource(),pos.getBlockPos(p),controller.getString(p),action.getString(p),0);
                            return 1;
                        }),
                        new CommandVariant(targets,info).execute((p)->{
                            for (Entity entity : targets.getEntities(p)) {
                                if (entity instanceof IActionListener actionListener){
                                    p.getSource().sendSuccess(formComponent(actionListener),true);
                                }
                            }
                            return 1;
                        }),
                        new CommandVariant(pos,info).execute((p)->{
                            BlockEntity blockEntity = p.getSource().getLevel().getBlockEntity(pos.getBlockPos(p));
                            if (blockEntity instanceof IActionListener actionListener){
                                p.getSource().sendSuccess(formComponent(actionListener),true);
                            }
                            return 1;
                        }))
                ;
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }

    private static Component formComponent(IActionListener actionListener){
        StringBuilder builder = new StringBuilder();
        int controllerCount = actionListener.getActionStateMachine().getControllers().size();
        builder.append("\n");
        builder.append("§lActions:§r");
        builder.append("\n");
        for (int i = 0; i < controllerCount; i++) {
            ActionController controller = actionListener.getActionStateMachine().getControllers().get(i);
            int actionsCount = controller.getActions().length;
            builder.append("§d");
            builder.append(controller.getName());
            builder.append("§f: \n[");
            for (int j = 0; j < actionsCount; j++) {
                Action action = controller.getActions()[j];
                builder.append("{");
                builder.append("§aName: §b");
                builder.append(action.getName());
                builder.append("§f; ");
                builder.append("§eLength: §b");
                builder.append(action.getLength());
                builder.append("§f}");
                if (j != actionsCount-1){
                    builder.append(", ");
                }
            }
            builder.append("§f]");
            if (i != controllerCount-1){
                builder.append("§f, ");
            }
        }
        return Component.literal(builder.toString());
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