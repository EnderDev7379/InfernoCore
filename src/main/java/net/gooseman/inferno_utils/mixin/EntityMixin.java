package net.gooseman.inferno_utils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Vec3 getForward();

    @Shadow public abstract void setDeltaMovement(double d, double e, double f);

    @Shadow public boolean hurtMarked;

    @Inject(method="handlePortal", at= @At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.teleport (Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    private void preventEndTeleportation(CallbackInfo callbackInfo, @Local TeleportTransition target) {
        InfernoConfig.reloadConfig();
        if (target.newLevel().dimension() == Level.END && InfernoConfig.config.getOrDefault("end_portal_disabled", true)) {
            Vec3 backwards = this.getForward().scale(-1);
            this.setDeltaMovement(backwards.x, 1, backwards.z);
            this.hurtMarked = true;
            callbackInfo.cancel();
        }
    }
}
