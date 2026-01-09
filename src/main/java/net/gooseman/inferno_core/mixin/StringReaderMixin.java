package net.gooseman.inferno_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.StringReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StringReader.class)
public class StringReaderMixin {
    @ModifyReturnValue(method = "isAllowedInUnquotedString", at = @At("RETURN"))
    private static boolean allowSectionSignInUnquotedString(boolean original, @Local(argsOnly = true) char c) {
        return original || c == '§';
    }
}
