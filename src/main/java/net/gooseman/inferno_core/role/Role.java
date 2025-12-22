package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.world.entity.player.Player;

public interface Role {
    String getId();
    Player getPlayer();
    boolean shouldTick();
    void tick();
    PlayerMana getDefaultMana();
    boolean is(RoleType<? extends Role> role);
    void reset();
}
