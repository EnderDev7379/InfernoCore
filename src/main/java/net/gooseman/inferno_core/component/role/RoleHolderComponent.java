package net.gooseman.inferno_core.component.role;

import net.gooseman.inferno_core.role.Role;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import static net.gooseman.inferno_core.InfernoCore.MOD_ID;

public interface RoleHolderComponent extends Component, ServerTickingComponent, AutoSyncedComponent {
    ComponentKey<RoleHolderComponent> KEY = ComponentRegistry.getOrCreate(ResourceLocation.parse(MOD_ID + ":role_holder"), RoleHolderComponent.class);

    Role get();
    void set(Role role);
}
