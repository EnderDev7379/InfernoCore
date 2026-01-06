package net.gooseman.inferno_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.gooseman.inferno_core.component.role.RoleHolderComponent;
import net.gooseman.inferno_core.role.MephistoRole;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.gooseman.inferno_core.InfernoCore.LOGGER;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    private Component modifyDisplayName(Component original) {
        if (RoleHolderComponent.KEY.get(this).get() instanceof MephistoRole role && role.mephistoMode) {
            LOGGER.info("mephisto guy");
            return Component.literal("mephisto guy");
        }
        return original;
    }
}
