package net.gooseman.inferno_utils.mana;

import net.minecraft.world.entity.player.Player;

public class PlayerMana extends Mana {
    Player player;
    public boolean shouldRender;

    public PlayerMana(Player player) { this(player, 0, 0, 100, 100); }

    public PlayerMana(Player player, float mana) { this(player, mana, 0, 100, 100); }

    public PlayerMana(Player player, float mana, float manaLimit) { this(player, mana, 0, manaLimit, manaLimit); }

    public PlayerMana(Player player, float mana, float manaLimit, float overflowLimit) { this(player, mana, 0, manaLimit, overflowLimit); }

    public PlayerMana(Player player, float mana, float underflowLimit, float manaLimit, float overflowLimit) {
        super(mana, underflowLimit, manaLimit, overflowLimit);
        this.player = player;
        this.shouldRender = false;
    }
}
