package net.gooseman.inferno_core.component;

import net.gooseman.inferno_core.component.mana.ManaHolderComponent;
import net.gooseman.inferno_core.component.mana.PlayerManaHolderComponent;
import net.gooseman.inferno_core.component.role.PlayerRoleHolderComponent;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ComponentInitializer implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(Player.class, RoleHolderComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).impl(PlayerRoleHolderComponent.class).end(PlayerRoleHolderComponent::new);
        registry.beginRegistration(Player.class, ManaHolderComponent.KEY).respawnStrategy(RespawnCopyStrategy.CHARACTER).after(RoleHolderComponent.KEY).impl(PlayerManaHolderComponent.class).end(PlayerManaHolderComponent::new);
    }
}
