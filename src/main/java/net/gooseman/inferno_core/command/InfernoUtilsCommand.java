package net.gooseman.inferno_core.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.gooseman.inferno_core.InfernoCore;
import net.gooseman.inferno_core.config.InfernoConfig;
import net.gooseman.inferno_core.component.mana.ManaHolderComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfernoUtilsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            LiteralCommandNode<CommandSourceStack> rootNode = Commands.literal(InfernoCore.MOD_ID).requires(Commands.hasPermission(3)).build();

            LiteralCommandNode<CommandSourceStack> versionNode = Commands.literal("version").executes(InfernoUtilsCommand::getVersion).build();

            LiteralCommandNode<CommandSourceStack> reloadConfigNode = Commands.literal("reloadConfig").executes(InfernoUtilsCommand::reloadConfig).build();

            LiteralCommandNode<CommandSourceStack> setManaNode = Commands.literal("set_mana").build();
            ArgumentCommandNode<CommandSourceStack, Float> setManaValueNode = Commands.argument("set_mana_value", FloatArgumentType.floatArg()).executes(InfernoUtilsCommand::setMana).build();

            LiteralCommandNode<CommandSourceStack> getManaNode = Commands.literal("get_mana").executes(InfernoUtilsCommand::getMana).build();

            LiteralCommandNode<CommandSourceStack> combatAddNode = Commands.literal("combat_add").executes(InfernoUtilsCommand::combatAdd).build();

            commandDispatcher.getRoot().addChild(rootNode);

            rootNode.addChild(versionNode);
            rootNode.addChild(reloadConfigNode);

            rootNode.addChild(setManaNode);
            setManaNode.addChild(setManaValueNode);

            rootNode.addChild(getManaNode);

            rootNode.addChild(combatAddNode);
        });
    }

    public static int getVersion(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal(FabricLoader.getInstance().getModContainer(InfernoCore.MOD_ID).get().getMetadata().getVersion().getFriendlyString()), false);
        return 1;
    }

    public static int reloadConfig(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().hasPermission(4)) {
            context.getSource().sendFailure(Component.literal("You do not have permission to run this command!"));
            return -1;
        }
        InfernoConfig.reloadConfig();
        context.getSource().sendSuccess(() -> Component.literal("Configuration file reloaded!"), true);
        return 1;
    }

    public static int setMana(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            return -1;
        }
        ManaHolderComponent.KEY.get(context.getSource().getEntity()).get().setMana(context.getArgument("set_mana_value", Float.class), true, true);

        context.getSource().sendSuccess(() -> Component.literal("Your mana is set to " + ManaHolderComponent.KEY.maybeGet(context.getSource().getEntity()).map(holder -> holder.get().getMana()).orElse(69420f)), false);
        return 1;
    }

    public static int getMana(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer()) {
            return -1;
        }
        context.getSource().sendSuccess(() -> Component.literal("Your mana is " + ManaHolderComponent.KEY.maybeGet(context.getSource().getEntity()).map(holder -> holder.get().getMana()).orElse(69420f)), false);
        return 1;
    }

    public static int combatAdd(CommandContext<CommandSourceStack> context) {
        if (!context.getSource().isPlayer())
            return -1;

        ServerPlayer player = context.getSource().getPlayer();

        long currentTime = context.getSource().getLevel().getGameTime();
        InfernoCore.playerCombatTracker.put(player.getStringUUID(), currentTime);

        List<UUID> forRemoval = new ArrayList<>();
        InfernoCore.combatBossEvents.forEach(((uuid, serverBossEvent) -> {
            serverBossEvent.removePlayer(player);
            if (serverBossEvent.getPlayers().isEmpty())
                forRemoval.add(uuid);
        }));
        forRemoval.forEach(uuid -> InfernoCore.combatBossEvents.remove(uuid));

        ServerBossEvent combatBossEvent = new ServerBossEvent(Component.literal("In Combat"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_20);
        InfernoCore.combatBossEvents.put(combatBossEvent.getId(), combatBossEvent);

        combatBossEvent.addPlayer(player);
        InfernoCore.playerCombatBossEvents.put(player.getUUID(), combatBossEvent.getId());

        return 1;
    }
}
