package net.gooseman.inferno_core;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.gooseman.inferno_core.component.mana.ManaHolderComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import static net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDamage;
import static net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDeath;
import static net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDamage;
import static net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;

public class ModDynamicEventHandlers {
    private static <T> T getEventHandler(Entity entity, Class<T> functionalInterface) {
        if (!(entity instanceof ServerPlayer player)) return null;
        if (!functionalInterface.isAnnotationPresent(FunctionalInterface.class)) {
            throw new IllegalArgumentException("Not a functional interface");
        }
        return ManaHolderComponent.KEY.get(player).get().getEventHandler(functionalInterface);
    }
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            AllowDamage victimEventHandler = getEventHandler(entity, AllowDamage.class);
            AllowDamage attackerEventHandler = getEventHandler(source.getEntity(), AllowDamage.class);
            return (victimEventHandler != null ? victimEventHandler.allowDamage(entity, source, amount) : true) &&
                    (attackerEventHandler != null ? attackerEventHandler.allowDamage(entity, source, amount) : true);
        });
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            AllowDeath victimEventHandler = getEventHandler(entity, AllowDeath.class);
            AllowDeath attackerEventHandler = getEventHandler(source.getEntity(), AllowDeath.class);
            return (victimEventHandler != null ? victimEventHandler.allowDeath(entity, source, amount) : true) &&
                    (attackerEventHandler != null ? attackerEventHandler.allowDeath(entity, source, amount) : true);
        });
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
            AfterDamage victimEventHandler = getEventHandler(entity, AfterDamage.class);
            AfterDamage attackerEventHandler = getEventHandler(source.getEntity(), AfterDamage.class);
            if (victimEventHandler != null) victimEventHandler.afterDamage(entity, source, baseDamageTaken, damageTaken, blocked);
            if (attackerEventHandler != null) attackerEventHandler.afterDamage(entity, source, baseDamageTaken, damageTaken, blocked);
        });
        ServerLivingEntityEvents.AFTER_DEATH.register(((entity, source) -> {
            AfterDeath victimEventHandler = getEventHandler(entity, AfterDeath.class);
            AfterDeath attackerEventHandler = getEventHandler(source.getEntity(), AfterDeath.class);
            if (victimEventHandler != null) victimEventHandler.afterDeath(entity, source);
            if (attackerEventHandler != null) attackerEventHandler.afterDeath(entity, source);
        }));
    }
}
