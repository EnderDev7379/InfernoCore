package net.gooseman.inferno_core.mana;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerMana extends Mana {
    ServerPlayer player;
    public boolean shouldRender;
    public boolean preventDamageTilt;

    public PlayerMana(ServerPlayer player) { this(player, 0, 0, 100, 100); }

    public PlayerMana(ServerPlayer player, float mana) { this(player, mana, 0, 100, 100); }

    public PlayerMana(ServerPlayer player, float mana, float manaLimit) { this(player, mana, 0, manaLimit, manaLimit); }

    public PlayerMana(ServerPlayer player, float mana, float manaLimit, float overflowLimit) { this(player, mana, 0, manaLimit, overflowLimit); }

    public PlayerMana(ServerPlayer player, float mana, float underflowLimit, float manaLimit, float overflowLimit) {
        super(mana, underflowLimit, manaLimit, overflowLimit);
        this.player = player;
        this.shouldRender = false;
        this.preventDamageTilt = false;
    }
}
