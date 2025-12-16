package fuzs.resourcepackoverrides.fabric.client;

import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.input.KeyEvent;

public class ResourcePackOverridesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ScreenEvents.AFTER_INIT.register((Minecraft client, Screen screen, int scaledWidth, int scaledHeight) -> {
            if (screen instanceof PackSelectionScreen) {
                ScreenEvents.afterRender(screen)
                        .register((Screen packSelectionScreen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) -> {
                            PackActionsHandler.onBeforeRenderScreen(Screens.getClient(packSelectionScreen),
                                    (PackSelectionScreen) packSelectionScreen,
                                    guiGraphics,
                                    mouseX,
                                    mouseY,
                                    partialTick);
                        });
                ScreenKeyboardEvents.afterKeyPress(screen).register((Screen packSelectionScreen, KeyEvent keyEvent) -> {
                    PackActionsHandler.onAfterKeyPressed(Screens.getClient(packSelectionScreen),
                            (PackSelectionScreen) packSelectionScreen,
                            keyEvent);
                });
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(PackActionsHandler::onEndClientTick);
    }
}
