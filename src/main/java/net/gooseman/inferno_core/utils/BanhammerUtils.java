package net.gooseman.inferno_core.utils;

import eu.pb4.banhammer.api.BanHammer;
import eu.pb4.banhammer.api.PunishmentData;
import eu.pb4.banhammer.api.PunishmentType;
import eu.pb4.banhammer.impl.BHUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.gooseman.inferno_core.config.InfernoConfig;
import net.minecraft.server.level.ServerPlayer;

import static net.gooseman.inferno_core.InfernoCore.LOGGER;

public class BanhammerUtils {
    public static void temporaryBan(ServerPlayer player, String timeKey, String reasonKey) {
        if (!FabricLoader.getInstance().isModLoaded("banhammer")) {
            LOGGER.error("Banhammer is not present! Couldn't ban player");
            return;
        }

        InfernoConfig.reloadConfig();

        BanHammer.punish(PunishmentData.create(
                player,
                player.getServer().createCommandSourceStack(),
                InfernoConfig.config.getOrDefault(reasonKey, "You have died/combat logged"),
                BHUtils.parseDuration(InfernoConfig.config.getOrDefault(timeKey, "8h")),
                PunishmentType.BAN
        ), true, false);
    }
}
