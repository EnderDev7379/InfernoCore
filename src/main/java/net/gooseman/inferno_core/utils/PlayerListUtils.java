package net.gooseman.inferno_core.utils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.function.Predicate;

public class PlayerListUtils {
    public static void broadcastNonAdmins(PlayerList playerList, Packet<?> packet) {
        broadcastFiltered(playerList, packet, player -> player.getPermissionLevel() < 3);
    }

    public static void broadcastAdmins(PlayerList playerList, Packet<?> packet) {
        broadcastFiltered(playerList, packet, player -> player.getPermissionLevel() >= 3);
    }

    public static void broadcastFiltered(PlayerList playerList, Packet<?> packet, Predicate<ServerPlayer> playerPredicate) {
        for (ServerPlayer player : playerList.getPlayers()) {
            if (playerPredicate.test(player)) {
                player.connection.send(packet);
            }
        }
    }
}
