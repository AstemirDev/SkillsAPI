package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.StringUtils;
import org.astemir.api.common.animation.Animation;
import org.astemir.api.common.commands.build.CommandArgument;
import org.astemir.api.common.commands.build.CommandBuilder;
import org.astemir.api.common.commands.build.CommandPart;
import org.astemir.api.common.commands.build.CommandVariant;
import org.astemir.api.common.action.Action;
import org.astemir.api.common.action.ActionController;
import org.astemir.api.common.action.IActionListener;

import java.util.Collection;


public class PlayActionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandArgument targets = CommandArgument.entities("targets");
        CommandArgument pos = CommandArgument.blockPos("pos");
        CommandArgument controller = CommandArgument.word("controller");
        CommandArgument action = CommandArgument.word("action");
        CommandArgument delay = CommandArgument.integer("delay",0);

        CommandBuilder builder = new CommandBuilder("action")
                .variants(
                        new CommandVariant(CommandPart.create("play"),targets,controller,action,delay).execute((p)->{
                            playAction(p.getSource(),targets.getEntities(p),controller.getString(p),action.getString(p),delay.getInt(p));
                            return 1;
                        }),new CommandVariant(CommandPart.create("play"),pos,controller,action,delay).execute((p)->{
                            playAction(p.getSource(),pos.getBlockPos(p),controller.getString(p),action.getString(p),delay.getInt(p));
                            return 1;
                        }),
                        new CommandVariant(CommandPart.create("play"),targets,controller,action).execute((p)->{
                            playAction(p.getSource(),targets.getEntities(p),controller.getString(p),action.getString(p),0);
                            return 1;
                        }),new CommandVariant(CommandPart.create("play"),pos,controller,action).execute((p)->{
                            playAction(p.getSource(),pos.getBlockPos(p),controller.getString(p),action.getString(p),0);
                            return 1;
                        }),
                        new CommandVariant(CommandPart.create("stop"),targets,controller).execute((p)->{
                            stopAction(p.getSource(),targets.getEntities(p),controller.getString(p));
                            return 1;
                        }),new CommandVariant(CommandPart.create("stop"),pos,controller).execute((p)->{
                            stopAction(p.getSource(),pos.getBlockPos(p),controller.getString(p));
                            return 1;
                        }),
                        new CommandVariant(CommandPart.create("info"),targets).execute((p)->{
                            for (Entity entity : targets.getEntities(p)) {
                                if (entity instanceof IActionListener actionListener){
                                    p.getSource().sendSuccess(formComponent(actionListener),true);
                                }
                            }
                            return 1;
                        }),
                        new CommandVariant(CommandPart.create("info"),pos).execute((p)->{
                            BlockEntity blockEntity = p.getSource().getLevel().getBlockEntity(pos.getBlockPos(p));
                            if (blockEntity instanceof IActionListener actionListener){
                                p.getSource().sendSuccess(formComponent(actionListener),true);
                            }
                            return 1;
                        }));
        dispatcher.register(builder.permission((p)->p.hasPermission(2)).build());
    }

    private static Component formComponent(IActionListener actionListener){
        int controllerCount = actionListener.getActionStateMachine().getControllers().size();
        MutableComponent result = Component.literal("§lControllers§r: \n");
        for (int i = 0; i < controllerCount; i++) {
            String id = (actionListener instanceof Entity entity) ? String.valueOf(entity.getUUID()) : (actionListener instanceof BlockEntity blockEntity) ? blockEntity.getBlockPos().getX()+" "+blockEntity.getBlockPos().getY()+" "+blockEntity.getBlockPos().getZ() : "null";
            ActionController controller = actionListener.getActionStateMachine().getControllers().get(i);
            String controllerName = StringUtils.capitalize(controller.getName());
            result.append(controllerName+": ");
            for (int j = 0; j < controller.getActions().length; j++) {
                Action action = controller.getActions()[j];
                String actionName = StringUtils.capitalize(action.getName());
                Component mutableComponent = Component.literal(
                        String.format("Name: %s\nLength: %s",
                                action.getName(),
                                action.getLength()));
                MutableComponent mutableComponent2 = Component.literal(String.format("%s",actionName)).
                        withStyle(Style.EMPTY.
                                withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,mutableComponent)).
                                withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/action play %s %s %s".formatted(id,controller.getName(),action.getName())))
                        );
                if (j != controller.getActions().length - 1) {
                    mutableComponent2.append(", ");
                }
                result.append(mutableComponent2);
            }
            if (i != controllerCount - 1) {
                result.append("\n");
            }
        }
        return result;
    }

    public static int stopAction(CommandSourceStack source, BlockPos pos, String controllerName){
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
        if (blockEntity instanceof IActionListener){
            stopAction(((IActionListener) blockEntity),controllerName);
        }
        return 1;
    }

    public static int stopAction(CommandSourceStack source, Collection<? extends Entity> entities, String controllerName){
        for (Entity entity : entities) {
            if (entity instanceof IActionListener){
                stopAction((IActionListener) entity,controllerName);
            }
        }
        return 1;
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

    public static void stopAction(IActionListener listener,String controllerName){
        for (ActionController controller : listener.getActionStateMachine().getControllers()) {
            if (controller.getName().equals(controllerName)){
                controller.setNoState();
            }
        }
    }

}