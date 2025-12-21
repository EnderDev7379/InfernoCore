package net.gooseman.inferno_utils.mixin;

import net.gooseman.inferno_utils.component.role.RoleHolderComponent;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.gooseman.inferno_utils.InfernoUtils.playerCombatTracker;

@Mixin(targets = "net.minecraft.world.effect.RegenerationMobEffect")
public class RegenerationEffectMixin {
    @Inject(method="applyEffectTick", at = @At(value = "HEAD"), cancellable = true)
    private void applyEffectTickFiltered(ServerLevel serverLevel, LivingEntity livingEntity, int i, CallbackInfoReturnable<Boolean> cir) {
        if (RoleHolderComponent.KEY.get(livingEntity).get().getId().equals("mephisto") &&
                livingEntity.level().getGameTime() - playerCombatTracker.getOrDefault(livingEntity.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
