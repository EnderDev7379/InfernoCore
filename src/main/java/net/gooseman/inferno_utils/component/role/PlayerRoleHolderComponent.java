package net.gooseman.inferno_utils.component.role;

import net.gooseman.inferno_utils.InfernoUtils;
import net.gooseman.inferno_utils.ModRegistries;
import net.gooseman.inferno_utils.component.mana.ManaHolderComponent;
import net.gooseman.inferno_utils.component.mana.PlayerManaHolderComponent;
import net.gooseman.inferno_utils.role.BaseRole;
import net.gooseman.inferno_utils.role.Role;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import static net.gooseman.inferno_utils.InfernoUtils.LOGGER;

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
        this.role = ModRegistries.ROLE.get(ResourceLocation.parse(readView.getStringOr("role_id", "inferno_utils:base"))).orElseThrow().value().create(player);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putString("role_id", InfernoUtils.MOD_ID + ":" + role.getId());
    }
}
