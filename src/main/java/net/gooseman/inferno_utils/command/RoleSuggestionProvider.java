package net.gooseman.inferno_utils.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.gooseman.inferno_utils.ModRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class RoleSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        //ResourceLocation roleTypeId =  ResourceLocationArgument.getId(context, "role");
        //RoleType<? > roleType = ModRegistries.ROLE.getOptional(roleTypeId).orElse(null);
        for (ResourceLocation resourceLocation : ModRegistries.ROLE.keySet())
            if (resourceLocation != null && resourceLocation.toString().contains(builder.getRemaining()))
                builder.suggest(resourceLocation.toString());

        return builder.buildFuture();
    }
}
