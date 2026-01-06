package net.gooseman.inferno_core.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RoleUtilsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal("roleUtils").requires((commandSourceStack -> (commandSourceStack.isPlayer() && !RoleHolderComponent.KEY.get(commandSourceStack.getPlayer()).get().getId().equals("base")))).build();
            LiteralCommandNode<CommandSourceStack> toggleMephisto = Commands.literal("toggleMephisto").requires(commandSourceStack -> (commandSourceStack.isPlayer()) && RoleHolderComponent.KEY.get(commandSourceStack.getPlayer()).get().getId().equals("mephisto")).executes(RoleUtilsCommand::toggleBigMepMode).build();

            commandDispatcher.getRoot().addChild(rootNode);
            rootNode.addChild(toggleMephisto);
        });
    }

    public static int toggleBigMepMode(CommandContext<CommandSourceStack> context) {
        if (!(RoleHolderComponent.KEY.get(context.getSource().getPlayer()).get() instanceof MephistoRole role)) return -1;
        role.mephistoMode = !role.mephistoMode;
        return 1;
    }
}
