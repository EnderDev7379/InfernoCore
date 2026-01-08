package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.minecraft.world.entity.player.Player;

public enum PlayerRoleState {
    PEASANT (BaseRole.class),
    INVISIBLE (MephistoRole.class),
    MEPHISTO (MephistoRole.class);

    private Class<? extends Role> requiredRole = Role.class;
    PlayerRoleState(Class<? extends Role> requiredRole) {
        this.requiredRole = requiredRole;
    }

    public boolean hasRequiredRole(Player player) {
        return requiredRole.isInstance(RoleHolderComponent.KEY.get(player).get());
    }
}
