package net.gooseman.inferno_core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow @Final private List<ServerPlayer> players;

    @Shadow public abstract void broadcastAll(Packet<?> packet);

    @Shadow public abstract String[] getPlayerNamesArray();

    @Unique
    public void broadcastAdmins(Packet<?> packet) {
        for (ServerPlayer player : this.players) {
            if (player.getPermissionLevel() >= 2) {
                player.connection.send(packet);
            }
        }
    }

    @Unique
    public void broadcastNonAdmins(Packet<?> packet) {
        for (ServerPlayer player : this.players) {
            if (player.getPermissionLevel() < 2) {
                player.connection.send(packet);
            }
        }
    }

    @Redirect(method="placeNewPlayer", at= @At(value = "INVOKE", target = "net/minecraft/server/players/PlayerList.broadcastAll (Lnet/minecraft/network/protocol/Packet;)V"))
    private void broadcastNewPlayer(PlayerList instance, Packet<?> packet, @Local(argsOnly = true) ServerPlayer player) {
        switch (RoleHolderComponent.KEY.get(player).get().getRoleState()) {
            case INVISIBLE: broadcastAdmins(packet);
            case MEPHISTO:
                player.setCustomName(Component.literal(String.format("§kMephisto§r (%s)", player.getName())));
                broadcastAdmins(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
                player.setCustomName(Component.literal("§kMephisto"));
                broadcastNonAdmins(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player)));
            default: broadcastAll(packet);
        }
    }
}
