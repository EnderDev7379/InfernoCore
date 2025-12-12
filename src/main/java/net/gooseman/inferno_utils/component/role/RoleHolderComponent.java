package net.gooseman.inferno_utils.component.role;

import net.gooseman.inferno_utils.role.Role;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public interface RoleHolderComponent extends Component, ServerTickingComponent {
    ComponentKey<RoleHolderComponent> KEY = ComponentRegistry.getOrCreate(ResourceLocation.parse("inferno_utils:role_holder"), RoleHolderComponent.class);

    Role get();
    void set(Role role);
}
