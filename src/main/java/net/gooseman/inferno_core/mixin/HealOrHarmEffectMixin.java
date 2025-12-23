package net.gooseman.inferno_core.mixin;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.config.InfernoConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.gooseman.inferno_core.InfernoCore.playerCombatTracker;

@Mixin(targets = "net.minecraft.world.effect.HealOrHarmMobEffect")
public class HealOrHarmEffectMixin {
    @Inject(method = "applyEffectTick", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.heal (F)V"), cancellable = true)
    private void applyEffectFilter(ServerLevel serverLevel, LivingEntity livingEntity, int i, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Player player &&
                RoleHolderComponent.KEY.get(player).get().getId().equals("mephisto") &&
                livingEntity.level().getGameTime() - playerCombatTracker.getOrDefault(livingEntity.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "applyInstantenousEffect", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.heal (F)V"), cancellable = true)
    private void applyInstantaneousEffectFilter(ServerLevel serverLevel, Entity entity, Entity entity2, LivingEntity livingEntity, int i, double d, CallbackInfo ci) {
        if (livingEntity instanceof Player player &&
                RoleHolderComponent.KEY.get(player).get().getId().equals("mephisto") &&
                livingEntity.level().getGameTime() - playerCombatTracker.getOrDefault(livingEntity.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400)) {
            ci.cancel();
        }
    }
}
