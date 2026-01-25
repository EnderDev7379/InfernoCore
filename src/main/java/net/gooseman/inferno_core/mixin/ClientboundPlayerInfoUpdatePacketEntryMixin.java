package net.gooseman.inferno_core.mixin;

import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.PlayerRoleState;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientboundPlayerInfoUpdatePacket.Entry.class)
public class ClientboundPlayerInfoUpdatePacketEntryMixin {
    @ModifyConstant(method = "<init>(Lnet/minecraft/server/level/ServerPlayer;)V", constant = @Constant(intValue = 1))
    private static int modifyListed(int constant, ServerPlayer player) {
        if (RoleHolderComponent.KEY.get(player).get().getRoleState() == PlayerRoleState.INVISIBLE) return 0;
        else return 1;
    }
}
