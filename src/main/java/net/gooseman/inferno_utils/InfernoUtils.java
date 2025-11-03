package net.gooseman.inferno_utils;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class InfernoUtils implements ModInitializer {
	public static final String MOD_ID = "inferno_utils";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.



		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (!world.isClient && player.getGameMode() != GameMode.SPECTATOR && player.getStackInHand(hand).isOf(Items.FIREWORK_ROCKET)) {
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
        });

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof PlayerEntity playerEntity) {
				playerEntity.sendMessage(Text.of("booo hooo, let me play a sad song for you on the world's smallest violin"), false);
				String deathTypeId = damageSource.getTypeRegistryEntry().getIdAsString();
				Entity attacker = damageSource.getAttacker();
				List<String> banExclusions = List.of(InfernoConfig.getStringArray("ban_exclusions"));
				playerEntity.sendMessage(Text.of(String.format("Attacker is player:%b;Isn't excluded:%b;Attacker isn't victim:%b", attacker instanceof PlayerEntity, !banExclusions.contains(deathTypeId), attacker != entity)), false);
				playerEntity.sendMessage(Text.of("DeathType id:" + deathTypeId), false);
				if ((attacker instanceof PlayerEntity || !banExclusions.contains(deathTypeId)) && attacker != entity) {
					playerEntity.sendMessage(Text.of("Death by player"), false);
				} else {
					playerEntity.sendMessage(Text.of("Git gud scrub"), false);
				}
			}
		});
	}
}