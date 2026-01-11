package net.gooseman.inferno_core.role;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.minecraft.server.level.ServerPlayer;

public enum PlayerRoleState {
    PEASANT(BaseRole.class),
    INVISIBLE(MephistoRole.class),
    MEPHISTO(MephistoRole.class);

    private final Class<? extends Role> requiredRole;

    PlayerRoleState(Class<? extends Role> requiredRole) {
        this.requiredRole = requiredRole;
    }

    public boolean hasRequiredRole(ServerPlayer player) {
        Role playerRole = RoleHolderComponent.KEY.get(player).get();
        return requiredRole.isInstance(playerRole);
    }
}
