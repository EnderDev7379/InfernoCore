package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.MephistoMana;
import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

public class MephistoRole extends BaseRole {
    public MephistoRole(ServerPlayer player) {
        super(player);
    }

    @Override
    public String getId() {
        return "mephisto";
    }

    @Override
    public PlayerMana getDefaultMana() {
        return new MephistoMana(player, 0, -50, 100, 200);
    }

    @Override
    public void reset() {
        super.reset();
    }
}
