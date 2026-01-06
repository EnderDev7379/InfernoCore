package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.MephistoMana;
import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MephistoRole extends BaseRole {
    public boolean mephistoMode = true;

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

    @Override
    public void readAdditional(ValueInput readView) {
        mephistoMode = readView.getBooleanOr("mephistoMode", true);
    }

    @Override
    public void writeAdditional(ValueOutput writeView) {
        writeView.putBoolean("mephistoMode", mephistoMode);
    }
}
