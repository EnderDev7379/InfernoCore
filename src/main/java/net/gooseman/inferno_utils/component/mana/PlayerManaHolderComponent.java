package net.gooseman.inferno_utils.component.mana;

import net.gooseman.inferno_utils.mana.Mana;
import net.gooseman.inferno_utils.mana.PlayerMana;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import static net.gooseman.inferno_utils.InfernoUtils.LOGGER;

public class PlayerManaHolderComponent extends BaseManaHolderComponent implements AutoSyncedComponent {
    Player player;

    public PlayerManaHolderComponent(Player player) {
        this.player = player;
        mana = new PlayerMana(player);
    }

    @Override
    public PlayerMana get() {
        return (PlayerMana) this.mana;
    }

    @Override
    public void set(Mana mana) {
        if (mana instanceof PlayerMana playerMana) {
            this.mana = playerMana;
            ManaHolderComponent.KEY.sync(player);
        }
    }

    public void overrideMana(PlayerMana newMana) { overrideMana(newMana, false); }

    public void overrideMana(PlayerMana newMana, boolean keepMana) {
        PlayerMana oldMana = get();

        set(newMana);

        if (keepMana) {
            mana.setUnderflowLimit(oldMana.getUnderflowLimit());
            mana.setOverflowLimit(oldMana.getOverflowLimit());
            mana.setManaLimit(oldMana.getManaLimit());
            mana.setMana(oldMana.getMana(), true, true);
            ManaHolderComponent.KEY.sync(player);
        }
    }

    @Override
    public boolean isRequiredOnClient() {
        return false;
    }

    @Override
    public void writeSyncPacket(RegistryFriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeFloat(mana.getUnderflowLimit());
        buf.writeFloat(mana.getOverflowLimit());
        buf.writeFloat(mana.getManaLimit());
        buf.writeFloat(mana.getMana());
        buf.writeBoolean(get().shouldRender);
    }

    @Override
    public void applySyncPacket(RegistryFriendlyByteBuf buf) {
        mana.setUnderflowLimit(buf.readFloat());
        mana.setOverflowLimit(buf.readFloat());
        mana.setManaLimit(buf.readFloat());
        mana.setMana(buf.readFloat(), true, true);
        get().shouldRender = buf.readBoolean();
    }
}
