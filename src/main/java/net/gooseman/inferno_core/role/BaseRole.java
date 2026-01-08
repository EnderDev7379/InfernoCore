package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;
import java.util.Optional;

public class BaseRole implements Role {
    protected ServerPlayer player;
    protected PlayerRoleState roleState;

    public BaseRole(ServerPlayer player) {
        this.player = player;
        this.roleState = PlayerRoleState.PEASANT;
    }

    @Override
    public String getId() {
        return "base";
    }

    @Override
    public ServerPlayer getPlayer() {
        return null;
    }

    @Override
    public boolean shouldTick() {
        return false;
    }

    @Override
    public void tick() {}

    @Override
    public PlayerMana getDefaultMana() {
        return new PlayerMana(player, 0, 0);
    }

    @Override
    public void reset() {}

    @Override
    public PlayerRoleState getRoleState() {
        return roleState;
    }

    @Override
    public void readAdditional(ValueInput readView) {
        roleState = PlayerRoleState.values()[readView.getIntOr("roleState", PlayerRoleState.PEASANT.ordinal())];
    }

    @Override
    public void writeAdditional(ValueOutput writeView) {
        writeView.putInt("roleState", roleState.ordinal());
    }

    @Override
    public void setRoleState(PlayerRoleState newState) {
        if (roleState == newState) return;
        switch (newState) {
            case INVISIBLE:
                player.getServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
            case MEPHISTO:
                PlayerList playerList = player.getServer().getPlayerList();
                if (roleState != PlayerRoleState.INVISIBLE) player.getServer().getPlayerList().broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
                player.setCustomName(Component.literal(String.format("§kMephisto§r (%s)", player.getName())));
                playerList.broadcastAdmins(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
                player.setCustomName(Component.literal("§kMephisto"));
                playerList.broadcastNonAdmins(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
            default:
                player.getServer().getPlayerList().broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
        }
    }
}
