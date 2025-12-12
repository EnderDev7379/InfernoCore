package net.gooseman.inferno_utils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.gooseman.inferno_utils.component.mana.ManaHolderComponent;
import net.gooseman.inferno_utils.component.mana.PlayerManaHolderComponent;
import net.gooseman.inferno_utils.mana.PlayerMana;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

import static net.gooseman.inferno_utils.InfernoUtils.LOGGER;
import static net.gooseman.inferno_utils.InfernoUtils.MOD_ID;

public class InfernoUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, ResourceLocation.fromNamespaceAndPath(InfernoUtils.MOD_ID, "mana_bar"), InfernoUtilsClient::render);
    }

    private static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        guiGraphics.fillGradient(0, 0, 10, 10, 0xFFAA00FF, 0xFF0055CC);
        renderMana(guiGraphics);
    }

    private static void renderMana(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        PlayerManaHolderComponent manaHolder = (PlayerManaHolderComponent) ManaHolderComponent.KEY.get(minecraft.player);
        PlayerMana mana = manaHolder.get();
        if (!mana.shouldRender) return;
        int xOrigin = guiGraphics.guiWidth() / 2 + 91;
        int yOrigin = guiGraphics.guiHeight() - 59;

        ResourceLocation MANA_BAR_BACKGROUND = ResourceLocation.fromNamespaceAndPath(MOD_ID, "mana_bar_background");
        ResourceLocation MANA_BAR_PROGRESS = ResourceLocation.fromNamespaceAndPath(MOD_ID, "mana_bar_progress");

//        LOGGER.info("Current mana: {}\nMana Limit: {}", mana.getMana(), mana.getManaLimit());
        guiGraphics.fillGradient(xOrigin - 81, yOrigin, (int) (xOrigin - 81 + 81 * Math.clamp(mana.getMana() / mana.getManaLimit(), 0f, 1f)), yOrigin + 9, 0xFFAA00FF, 0xFF0055CC);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, MANA_BAR_BACKGROUND, xOrigin - 81, yOrigin, 81, 9);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, MANA_BAR_PROGRESS, 81, 9, 0, 0, xOrigin - 81, yOrigin, (int) (81 * Math.clamp(mana.getMana() / mana.getManaLimit(), 0f, 1f)), 9);
    }
}
