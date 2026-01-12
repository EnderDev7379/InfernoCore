package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.utils.PlayerListUtils;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.EnumSet;
import java.util.List;

import static net.gooseman.inferno_core.InfernoCore.LOGGER;

public enum PlayerRoleState {
    PEASANT(BaseRole.class),
    INVISIBLE(MephistoRole.class),
    MEPHISTO(MephistoRole.class);

    private final Class<? extends Role> requiredRole;

    PlayerRoleState(Class<? extends Role> requiredRole) {
        this.requiredRole = requiredRole;
    }

    public boolean hasRequiredRole(ServerPlayer player) {
        Role playerRole = RoleHolderComponent.KEY.get(player).get();
        return requiredRole.isInstance(playerRole);
    }

    public void initialise(ServerPlayer player, PlayerRoleState prevState) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        PlayerList playerList = server.getPlayerList();

        if (prevState == this) return;

        switch (this) {
            case PEASANT:
                if (prevState != INVISIBLE)
                    playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
                playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
            case INVISIBLE:
                PlayerListUtils.broadcastNonAdmins(playerList, new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
                PlayerListUtils.broadcastAdmins(playerList, new ClientboundPlayerInfoUpdatePacket(EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE), List.of(player)));
            case MEPHISTO:
                playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
                playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
            default:
                LOGGER.error("Unfinished/Unknown state {} assigned to player {}", this.name(), player.getDisplayName());
        }
    }
}
