package net.gooseman.inferno_utils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Vec3d getRotationVec(float tickProgress);

    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Shadow public boolean velocityModified;

    @Inject(method="tickPortalTeleportation", at= @At(value = "INVOKE", target = "net/minecraft/entity/Entity.teleportTo (Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;"), cancellable = true)
    private void preventEndTeleportation(CallbackInfo callbackInfo, @Local TeleportTarget target) {
        InfernoConfig.reloadConfig();
        if (target.world().getRegistryKey() == World.END && InfernoConfig.config.getOrDefault("end_portal_disabled", true)) {
            Vec3d backwards = this.getRotationVec(1.0F).multiply(-1);
            Vec3d velocity = new Vec3d(backwards.x, 1, backwards.z);
            this.setVelocity(velocity.x, velocity.y, velocity.z);
            this.velocityModified = true;
            callbackInfo.cancel();
        }
    }
}
