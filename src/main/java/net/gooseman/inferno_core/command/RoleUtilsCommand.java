package net.gooseman.inferno_core.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.gooseman.inferno_core.role.PlayerRoleState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class RoleUtilsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal("roleUtils").requires((commandSourceStack -> (commandSourceStack.isPlayer() && !RoleHolderComponent.KEY.get(commandSourceStack.getPlayer()).get().getId().equals("base")))).build();
            LiteralCommandNode<CommandSourceStack> changeRoleStateNode = Commands.literal("changeRoleState").build();
            ArgumentCommandNode<CommandSourceStack, String> roleStateNode = Commands.argument("roleState", StringArgumentType.word()).executes(RoleUtilsCommand::changeBigMepMode).build();
            commandDispatcher.getRoot().addChild(rootNode);
            rootNode.addChild(changeRoleStateNode);
            changeRoleStateNode.addChild(roleStateNode);
        });
    }

    public static int changeBigMepMode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
//        if (!context.getSource().isPlayer()) {
//            context.getSource().sendFailure(Component.literal("Source must be a player!"));
//            return -1;
//        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        PlayerRoleState roleState = PlayerRoleState.valueOf(StringArgumentType.getString(context, "roleState"));

        if (!roleState.hasRequiredRole(player)) {
            context.getSource().sendFailure(Component.literal("You don't have the role required to go into this state"));
        }

        RoleHolderComponent.KEY.get(player).get().setRoleState(roleState);

        return 1;
    }
}
