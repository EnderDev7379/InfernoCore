package net.gooseman.inferno_utils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
	@Inject(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/world/GameRules.getBoolean (Lnet/minecraft/world/GameRules$Key;)Z", shift = At.Shift.BY, by = 2))
	private void modifyShouldRegen(ServerPlayerEntity player, CallbackInfo callbackInfo, @Local LocalBooleanRef localRef) {
		localRef.set(localRef.get() && !player.getDisplayName().getString().equals(InfernoConfig.config.getOrDefault("mephisto", null)));
	}
}