package net.gooseman.inferno_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.gooseman.inferno_core.role.PlayerRoleState;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow protected abstract ServerStatus.Players buildPlayerStatus();

    @WrapWithCondition(method = "buildPlayerStatus", at = @At(value = "INVOKE", target = "it/unimi/dsi/fastutil/objects/ObjectArrayList.add (Ljava/lang/Object;)Z"))
    private <K> boolean shouldShowPlayer(ObjectArrayList<K> instance, K k, @Local ServerPlayer serverPlayer) {
        return (!(RoleHolderComponent.KEY.get(serverPlayer).get() instanceof MephistoRole mephistoRole) || mephistoRole.getRoleState() != PlayerRoleState.INVISIBLE);
    }
    @ModifyReturnValue(method = "getPlayerCount", at = @At(value= "RETURN"))
    private int modifyPlayerCount(int original) {
        return Math.min(original, buildPlayerStatus().online());
    }
}
