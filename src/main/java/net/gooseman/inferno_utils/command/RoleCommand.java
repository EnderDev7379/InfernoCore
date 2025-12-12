package net.gooseman.inferno_utils.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.gooseman.inferno_utils.ModRegistries;
import net.gooseman.inferno_utils.component.role.PlayerRoleHolderComponent;
import net.gooseman.inferno_utils.component.role.RoleHolderComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public class RoleCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = literal("role").requires(Commands.hasPermission(3)).build();
            ArgumentCommandNode<CommandSourceStack, EntitySelector> targetNode = Commands.argument("target", EntityArgument.player()).build();
            LiteralCommandNode<CommandSourceStack> setNode = literal("set").build();
            ArgumentCommandNode<CommandSourceStack, ResourceLocation> roleNode = argument("role", ResourceLocationArgument.id()).suggests(new RoleSuggestionProvider()).executes(RoleCommand::roleSet).build();
            ArgumentCommandNode<CommandSourceStack, Boolean> keepValuesNode = argument("keep_values", BoolArgumentType.bool()).executes(context -> roleSet(context, context.getArgument("keep_values", Boolean.class))).build();
            LiteralCommandNode<CommandSourceStack> getNode = literal("get").executes(RoleCommand::roleGet).build();


            commandDispatcher.getRoot().addChild(rootNode);
            rootNode.addChild(targetNode);

            targetNode.addChild(getNode);

            targetNode.addChild(setNode);
            setNode.addChild(roleNode);
            roleNode.addChild(keepValuesNode);
        }));
    }

    public static int roleSet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return roleSet(context, false);
    }

    public static int roleSet(CommandContext<CommandSourceStack> context, boolean keepValues) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        ResourceLocation roleTypeId = ResourceLocationArgument.getId(context, "role");
        PlayerRoleHolderComponent roleHolder = (PlayerRoleHolderComponent) RoleHolderComponent.KEY.get(player);
        roleHolder.set(ModRegistries.ROLE.get(roleTypeId).orElseThrow().value().create(player), keepValues);
        return 1;
    }

    public static int roleGet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "target");
        context.getSource().sendSuccess(() -> Component.literal(player.getDisplayName().getString() + "'s role is " + RoleHolderComponent.KEY.get(player).get().getId()), false);
        return 1;
    }
}
