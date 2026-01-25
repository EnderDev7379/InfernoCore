package net.gooseman.inferno_core.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.gooseman.inferno_core.InfernoCore;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.gooseman.inferno_core.role.PlayerRoleState;
import net.gooseman.inferno_core.role.Role;
import net.gooseman.inferno_core.utils.PlayerListUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import static net.gooseman.inferno_core.InfernoCore.LOGGER;

import java.util.List;

public class RoleUtilsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal("roleUtils").build();
            LiteralCommandNode<CommandSourceStack> changeMode = Commands.literal("changeMode").build();
            ArgumentCommandNode<CommandSourceStack, String> newMode = Commands.argument("newMode", StringArgumentType.word()).executes(RoleUtilsCommand::changeBigMepMode).build();

            commandDispatcher.getRoot().addChild(rootNode);
            rootNode.addChild(changeMode);
            changeMode.addChild(newMode);
        });
    }

    public static int changeBigMepMode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        assert player.getServer() != null;
        PlayerList playerList = player.getServer().getPlayerList();

        Role role = RoleHolderComponent.KEY.get(player).get();
        PlayerRoleState prevRoleState = role.getRoleState();
        PlayerRoleState newRoleState;

        String modeArgument = StringArgumentType.getString(context, "newMode").toUpperCase();
        try {
            newRoleState = PlayerRoleState.valueOf(modeArgument);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Mode {} does not exist", modeArgument);
            context.getSource().sendFailure(Component.literal("That mode does not exist!"));
            return -1;
        }

        if (!newRoleState.hasRequiredRole(player)) {
            context.getSource().sendFailure(Component.literal("You do not have the requirements to switch to this mode!"));
            return -1;
        }

        if (prevRoleState == newRoleState) {
            context.getSource().sendFailure(Component.literal("You are already in this mode!"));
        }

        role.setRoleState(newRoleState);

        switch (newRoleState) {
            case INVISIBLE:
                playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));

                InfernoCore.returnAdminName = true;
                PlayerListUtils.broadcastAdmins(playerList, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
                InfernoCore.returnAdminName = false;
            case MEPHISTO:
                playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));

                PlayerListUtils.broadcastNonAdmins(playerList, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
                InfernoCore.returnAdminName = true;
                PlayerListUtils.broadcastAdmins(playerList, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
                InfernoCore.returnAdminName = false;
            default:
                playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));

                playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
        }


        playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
        playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));


        return 1;
    }
}
