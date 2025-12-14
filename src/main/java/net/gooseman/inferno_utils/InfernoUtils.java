package net.gooseman.inferno_utils;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.gooseman.inferno_utils.command.InfernoUtilsCommand;
import net.gooseman.inferno_utils.command.RoleCommand;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.gooseman.inferno_utils.role.RoleType;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InfernoUtils implements ModInitializer {
	public static final String MOD_ID = "inferno_utils";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static HashMap<String, Long> playerCombatTracker = new HashMap<>();

	public static List<String> probableTraps = List.of(new String[]{"bad_respawn_point", "falling_anvil", "falling_stalactite", "fireworks", "stalagmite"});
	public static List<EntityType<?>> explosionExclusion = List.of(new EntityType<?>[]{EntityType.CREEPER, EntityType.GHAST, EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.END_CRYSTAL});

	public static HashMap<UUID, ServerBossEvent> combatBossEvents = new HashMap<>();
	public static HashMap<UUID, UUID> playerCombatBossEvents = new HashMap<>();

	public static boolean isMephisto(Player player) {
		return player.getDisplayName().getString().equals(InfernoConfig.config.getOrDefault("mephisto", "nobodyig"));
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		RoleType.register();

		if (!FabricLoader.getInstance().isModLoaded("banhammer")) LOGGER.warn("Banhammer is not installed! Some features will not function properly.");

		probableTraps = probableTraps.stream().map((str) -> "minecraft:"+str).toList();

		InfernoConfig.reloadConfig();

		InfernoUtilsCommand.register();
		RoleCommand.register();

		ModEventHandlers.register();
		ModDynamicEventHandlers.register();
	}
}