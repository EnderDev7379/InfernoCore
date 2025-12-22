package net.gooseman.inferno_core.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.gooseman.inferno_core.component.mana.PlayerManaHolderComponent;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Definition(id = "flashOnSetHealth", field = "Lnet/minecraft/client/player/LocalPlayer;flashOnSetHealth:Z")
    @Expression("this.flashOnSetHealth")
    @ModifyExpressionValue(method = "hurtTo", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean doDamageTilt(boolean original) {
        return original && !((PlayerManaHolderComponent) PlayerManaHolderComponent.KEY.get(this)).get().preventDamageTilt;
    }
}
