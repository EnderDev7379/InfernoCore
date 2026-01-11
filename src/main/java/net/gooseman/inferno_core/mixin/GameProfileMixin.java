package net.gooseman.inferno_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(GameProfile.class)
public class GameProfileMixin {
    @Shadow @Final private UUID id;

    @ModifyReturnValue(method = "getName", at = @At("RETURN"))
    private String getNameModifiedByRole(String original) {
        MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
        if (server == null) return original;
        PlayerList playerList = server.getPlayerList();
        if (playerList == null) return original;
        ServerPlayer player = playerList.getPlayer(id);
        if (player == null) return original;
        if (RoleHolderComponent.KEY.get(player).get() instanceof MephistoRole role && role.mephistoMode) return "§kMephisto§r";
        return original;
    }
}
