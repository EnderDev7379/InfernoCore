package net.gooseman.inferno_utils.role;

import net.gooseman.inferno_utils.ModRegistries;
import net.gooseman.inferno_utils.mana.PlayerMana;
import net.minecraft.world.entity.player.Player;

public class BaseRole implements Role {
    protected Player player;

    public BaseRole(Player player) {
        this.player = player;
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
    public boolean is(RoleType<? extends Role> roleType) {
        return ModRegistries.ROLE.getKey(roleType).toString().equals(getId());
    }

    @Override
    public void reset() {}
}
