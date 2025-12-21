package net.gooseman.inferno_utils.mixin;

import net.gooseman.inferno_utils.component.role.RoleHolderComponent;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.gooseman.inferno_utils.InfernoUtils.LOGGER;
import static net.gooseman.inferno_utils.InfernoUtils.playerCombatTracker;

@Mixin(targets = "net.minecraft.world.effect.HealOrHarmMobEffect")
public class HealOrHarmEffectMixin {
    @Inject(method = "applyEffectTick", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.heal (F)V"), cancellable = true)
    private void applyEffectFilter(ServerLevel serverLevel, LivingEntity livingEntity, int i, CallbackInfoReturnable<Boolean> cir) {
        if (RoleHolderComponent.KEY.get(livingEntity).get().getId().equals("mephisto") &&
                livingEntity.level().getGameTime() - playerCombatTracker.getOrDefault(livingEntity.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "applyInstantenousEffect", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.heal (F)V"), cancellable = true)
    private void applyInstantaneousEffectFilter(ServerLevel serverLevel, Entity entity, Entity entity2, LivingEntity livingEntity, int i, double d, CallbackInfo ci) {
        if (RoleHolderComponent.KEY.get(livingEntity).get().getId().equals("mephisto") &&
                livingEntity.level().getGameTime() - playerCombatTracker.getOrDefault(livingEntity.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400)) {
            ci.cancel();
        }
    }
}
