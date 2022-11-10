package org.astemir.api.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.astemir.api.common.PlayerScreenShaker;

import java.util.Collection;

public class ShakeScreenCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("shakescreen").requires((p)->p.hasPermission(2))
                .then(Commands.argument("targets",EntityArgument.players()).then(Commands.argument("power",DoubleArgumentType.doubleArg(0.0,1.0)).then(Commands.argument("ticks",IntegerArgumentType.integer()).executes((p)->
                    shakeScreen(p.getSource(),EntityArgument.getPlayers(p,"targets"),DoubleArgumentType.getDouble(p,"power"),IntegerArgumentType.getInteger(p,"ticks"))
        )))));
    }


    public static int shakeScreen(CommandSourceStack source, Collection<ServerPlayer> players, double power, int ticks){
        for (ServerPlayer player : players) {
            PlayerScreenShaker.shakeScreen(player, ticks, (float) power);
        }
        return 1;
    }
}