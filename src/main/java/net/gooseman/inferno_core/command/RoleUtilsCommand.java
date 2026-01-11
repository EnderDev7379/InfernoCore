package net.gooseman.inferno_core.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.EnumSet;
import java.util.List;

public class RoleUtilsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal("roleUtils").requires((commandSourceStack -> (commandSourceStack.isPlayer() && !RoleHolderComponent.KEY.get(commandSourceStack.getPlayer()).get().getId().equals("base")))).build();
            LiteralCommandNode<CommandSourceStack> toggleMephisto = Commands.literal("toggleMephisto").requires(commandSourceStack -> (commandSourceStack.isPlayer()) && RoleHolderComponent.KEY.get(commandSourceStack.getPlayer()).get().getId().equals("mephisto")).executes(RoleUtilsCommand::toggleBigMepMode).build();

            commandDispatcher.getRoot().addChild(rootNode);
            rootNode.addChild(toggleMephisto);
        });
    }

    public static int toggleBigMepMode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        if (!(RoleHolderComponent.KEY.get(player).get() instanceof MephistoRole role)) return -1;
        role.mephistoMode = !role.mephistoMode;
        assert player.getServer() != null;
        PlayerList playerList = player.getServer().getPlayerList();
        playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
        playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
        return 1;
    }
}
