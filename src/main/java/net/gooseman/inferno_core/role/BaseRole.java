package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.ModRegistries;
import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BaseRole implements Role {
    protected PlayerRoleState roleState;
    protected ServerPlayer player;

    public BaseRole(ServerPlayer player) {
        this.player = player;
        this.roleState = PlayerRoleState.PEASANT;
    }

    @Override
    public String getId() {
        return "base";
    }

    @Override
    public Player getPlayer() {
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
    public void reset() {
        if (roleState != PlayerRoleState.PEASANT) {
            assert player.getServer() != null;
            PlayerList playerList = player.getServer().getPlayerList();
            playerList.broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
            playerList.broadcastAll(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
        }
    }

    @Override
    public PlayerRoleState getRoleState() {
        return roleState;
    }

    @Override
    public void setRoleState(PlayerRoleState roleState) {
        this.roleState = roleState;
    }
}
