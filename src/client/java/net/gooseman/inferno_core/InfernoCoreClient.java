package net.gooseman.inferno_core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.gooseman.inferno_core.component.mana.ManaHolderComponent;
import net.gooseman.inferno_core.component.mana.PlayerManaHolderComponent;
import net.gooseman.inferno_core.mana.PlayerMana;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

import static net.gooseman.inferno_core.InfernoCore.MOD_ID;

public class InfernoCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.ARMOR_BAR, ResourceLocation.fromNamespaceAndPath(InfernoCore.MOD_ID, "mana_bar"), InfernoCoreClient::renderMana);
    }

//    private static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
//        guiGraphics.fillGradient(0, 0, 10, 10, 0xFFAA00FF, 0xFF0055CC);
//        renderMana(guiGraphics, deltaTracker);
//    }

    private static void renderMana(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        PlayerManaHolderComponent manaHolder = (PlayerManaHolderComponent) ManaHolderComponent.KEY.get(minecraft.player);
        PlayerMana mana = manaHolder.get();
        if (!mana.shouldRender) return;
        int xOrigin = guiGraphics.guiWidth() / 2 + 91;
        int yOrigin = guiGraphics.guiHeight() - 59;

        ResourceLocation MANA_BAR_BACKGROUND = ResourceLocation.fromNamespaceAndPath(MOD_ID, "mana_bar_background");
        ResourceLocation MANA_BAR_PROGRESS = ResourceLocation.fromNamespaceAndPath(MOD_ID, "mana_bar_progress");

        float currentMana = mana.getMana();
        float manaLimit = mana.getManaLimit();
        float overflowLimit = mana.getOverflowLimit();
        float underflowLimit = mana.getUnderflowLimit();

//        guiGraphics.fillGradient(xOrigin - 81, yOrigin, (int) (xOrigin - 81 + 81 * Math.clamp(mana.getMana() / mana.getManaLimit(), 0f, 1f)), yOrigin + 9, 0xFFAA00FF, 0xFF0055CC);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, MANA_BAR_BACKGROUND, xOrigin - 81, yOrigin, 81, 9);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, MANA_BAR_PROGRESS, 81, 9, 0, 0, xOrigin - 81, yOrigin, (int) (81 * Math.clamp(currentMana / manaLimit, 0f, 1f)), 9);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, MANA_BAR_PROGRESS, 81, 9, 0, 0, xOrigin - 81, yOrigin, (int) (81 * Math.clamp((currentMana - manaLimit) / (overflowLimit - manaLimit), 0f, 1f)), 9, Color.MAGENTA.getRGB());
    }
}
