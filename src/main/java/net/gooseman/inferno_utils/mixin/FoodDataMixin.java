package net.gooseman.inferno_utils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/world/level/GameRules.getBoolean (Lnet/minecraft/world/level/GameRules$Key;)Z", shift = At.Shift.BY, by = 2))
	private void modifyShouldRegen(ServerPlayer player, CallbackInfo callbackInfo, @Local LocalBooleanRef localRef) {
		localRef.set(localRef.get() && !player.getDisplayName().getString().equals(InfernoConfig.config.getOrDefault("mephisto", null)));
	}
}