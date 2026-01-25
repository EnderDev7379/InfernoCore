package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface Role {
    String getId();
    Player getPlayer();
    boolean shouldTick();
    void tick();
    PlayerMana getDefaultMana();
    void reset();
    default void readAdditional(ValueInput readView) {}
    default void writeAdditional(ValueOutput writeView) {}
    PlayerRoleState getRoleState();
    void setRoleState(PlayerRoleState roleState);
}
