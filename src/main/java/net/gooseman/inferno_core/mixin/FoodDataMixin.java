package net.gooseman.inferno_core.mixin;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.config.InfernoConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.gooseman.inferno_core.InfernoCore.playerCombatTracker;

@Mixin(FoodData.class)
public class FoodDataMixin {
	@ModifyVariable(method = "tick", at = @At(value = "STORE"))
	private boolean modifyShouldRegen(boolean shouldRegen, ServerPlayer player) {
		return shouldRegen && !(RoleHolderComponent.KEY.get(player).get().getId().equals("mephisto") &&
				player.level().getGameTime() - playerCombatTracker.getOrDefault(player.getStringUUID(), 0L) <= InfernoConfig.config.getOrDefault("combat_length", 400));
	}
}