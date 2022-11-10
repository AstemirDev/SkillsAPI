package org.astemir.api.common.commands.build;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.Predicate;

public class CommandBuilder {

    private String command;
    private Predicate<CommandSourceStack> permission = (p)->true;
    private CommandVariant[] branches;

    public CommandBuilder(String command) {
        this.command = command;
    }

    public CommandBuilder variants(CommandVariant... branches){
        this.branches = branches;
        return this;
    }

    public CommandBuilder permission(Predicate<CommandSourceStack> permission) {
        this.permission = permission;
        return this;
    }

    public LiteralArgumentBuilder build(){
        ArgumentBuilder res =  Commands.literal(command).requires(permission);
        for (CommandVariant branch : branches) {
            res = res.then(branch.build());
        }
        return (LiteralArgumentBuilder)res;
    }
}
