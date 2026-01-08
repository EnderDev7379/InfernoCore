package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface Role {
    String getId();
    ServerPlayer getPlayer();
    boolean shouldTick();
    void tick();
    PlayerMana getDefaultMana();
    void reset();
    void readAdditional(ValueInput readView);
    void writeAdditional(ValueOutput writeView);
    PlayerRoleState getRoleState();
    void setRoleState(PlayerRoleState newState);
}
