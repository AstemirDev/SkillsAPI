package org.astemir.api.common.commands.build;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import java.util.LinkedList;
import java.util.function.Predicate;

public class CommandBuilder extends CommandVariant{

    private String command;
    private Predicate<CommandSourceStack> permission = (p)->true;
    private LinkedList<CommandVariant> branches = new LinkedList<>();


    public CommandBuilder(String command) {
        this.command = command;
    }

    public CommandBuilder variants(CommandVariant... branches){
        for (CommandVariant branch : branches) {
            this.branches.add(branch);
        }
        return this;
    }

    public CommandBuilder permission(Predicate<CommandSourceStack> permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public LiteralArgumentBuilder build(){
        ArgumentBuilder res =  Commands.literal(command).requires(permission);
        for (CommandVariant branch : branches) {
            res = res.then(branch.build());
        }
        if (getArguments() != null && getExecutable() != null){
            res = mergeArguments(getArguments(),0);
        }else
        if (getExecutable() != null){
            res = res.executes(getExecutable());
        }
        return (LiteralArgumentBuilder)res;
    }

    public void addVariant(CommandVariant branch){
        this.branches.add(branch);
    }

}

