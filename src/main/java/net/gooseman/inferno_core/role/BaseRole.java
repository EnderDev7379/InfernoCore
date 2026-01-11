package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.ModRegistries;
import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class BaseRole implements Role {
    protected ServerPlayer player;

    public BaseRole(ServerPlayer player) {
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
    public void reset() {}
}
