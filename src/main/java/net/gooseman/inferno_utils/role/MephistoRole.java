package net.gooseman.inferno_utils.role;

import net.gooseman.inferno_utils.mana.MephistoMana;
import net.gooseman.inferno_utils.mana.PlayerMana;
import net.minecraft.world.entity.player.Player;

public class MephistoRole extends BaseRole {
    public MephistoRole(Player player) {
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
}
