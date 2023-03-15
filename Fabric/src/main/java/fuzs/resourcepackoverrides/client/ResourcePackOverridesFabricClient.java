package fuzs.resourcepackoverrides.client;

import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;

public class ResourcePackOverridesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof PackSelectionScreen) {
                ScreenEvents.afterRender(screen).register(PackActionsHandler::onScreen$Render$Post);
                ScreenKeyboardEvents.afterKeyPress(screen).register(PackActionsHandler::onKeyPressed$Post);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(PackActionsHandler::onClientTick$End);
    }
}
