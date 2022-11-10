package org.astemir.api.common.commands.build;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class CommandVariant {

    private CommandPart[] arguments;
    private Command<CommandSourceStack> executable;


    public CommandVariant(CommandPart... arguments) {
        this.arguments = arguments;
    }

    public CommandVariant execute(Command<CommandSourceStack> executable){
        this.executable = executable;
        return this;
    }

    public static CommandVariant arguments(CommandPart... arguments){
        CommandVariant branch = new CommandVariant(arguments);
        return branch;
    }


    public ArgumentBuilder build(){
        return mergeArguments(arguments,0);
    }

    public ArgumentBuilder mergeArguments(CommandPart[] arguments,int index){
        if (index < arguments.length-1) {
            ArgumentBuilder next = mergeArguments(arguments, index + 1);
            return arguments[index].build().then(next);
        }else{
            return arguments[index].build().executes(executable);
        }
    }
}
