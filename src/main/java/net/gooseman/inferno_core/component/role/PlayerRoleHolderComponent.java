package net.gooseman.inferno_core.component.role;

import net.gooseman.inferno_core.InfernoCore;
import net.gooseman.inferno_core.ModRegistries;
import net.gooseman.inferno_core.component.mana.ManaHolderComponent;
import net.gooseman.inferno_core.component.mana.PlayerManaHolderComponent;
import net.gooseman.inferno_core.role.BaseRole;
import net.gooseman.inferno_core.role.Role;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import static net.gooseman.inferno_core.InfernoCore.LOGGER;
import static net.gooseman.inferno_core.InfernoCore.MOD_ID;

public class PlayerRoleHolderComponent implements RoleHolderComponent {
    Role role;
    Player player;

    public PlayerRoleHolderComponent(Player player) {
        this.player = player;
        this.role = new BaseRole(player);
    }

    @Override
    public Role get() {
        return role;
    }

    @Override
    public void set(Role role) { set(role, false); }

    public void set(Role role, boolean keepValues) {
        if (role != this.role) {
            this.role.reset();
            this.role = role;
            LOGGER.info("{}'s role set to {}", player.getDisplayName(), role.getId());
            PlayerManaHolderComponent manaHolder = (PlayerManaHolderComponent) ManaHolderComponent.KEY.get(player);
            manaHolder.get().reset();
            manaHolder.overrideMana(role.getDefaultMana(), keepValues);
            LOGGER.info("Set mana = {underflowLimit: {}, manaLimit: {}, overflowLimit: {}, mana: {}}", manaHolder.get().getUnderflowLimit(), manaHolder.get().getManaLimit(), manaHolder.get().getOverflowLimit(), manaHolder.get().getMana());
        }
    }

    @Override
    public void serverTick() {
        if (role.shouldTick())
            role.tick();
    }

    @Override
    public void readData(ValueInput readView) {
        this.role = ModRegistries.ROLE.get(ResourceLocation.parse(readView.getStringOr("role_id", MOD_ID + ":base"))).orElseThrow().value().create(player);
        get().readAdditional(readView);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putString("role_id", InfernoCore.MOD_ID + ":" + role.getId());
        get().writeAdditional(writeView);
    }

    @Override
    public boolean isRequiredOnClient() {
        return false;
    }
}
