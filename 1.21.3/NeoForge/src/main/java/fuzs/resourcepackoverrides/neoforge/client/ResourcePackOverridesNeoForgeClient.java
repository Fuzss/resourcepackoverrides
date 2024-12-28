package fuzs.resourcepackoverrides.neoforge.client;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import fuzs.resourcepackoverrides.neoforge.data.client.ModLanguageProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = ResourcePackOverrides.MOD_ID, dist = Dist.CLIENT)
public class ResourcePackOverridesNeoForgeClient {

    public ResourcePackOverridesNeoForgeClient(ModContainer modContainer) {
        registerLoadingHandlers(modContainer.getEventBus());
        registerEventHandlers(NeoForge.EVENT_BUS);
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final GatherDataEvent evt) -> {
            evt.getGenerator()
                    .addProvider(evt.includeClient(),
                            new ModLanguageProvider(ResourcePackOverrides.MOD_ID, evt.getGenerator().getPackOutput())
                    );
        });
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final ScreenEvent.Render.Post evt) -> {
            if (evt.getScreen() instanceof PackSelectionScreen screen) {
                PackActionsHandler.onScreen$Render$Post(screen.getMinecraft(),
                        screen,
                        evt.getGuiGraphics(),
                        evt.getMouseX(),
                        evt.getMouseY(),
                        evt.getPartialTick()
                );
            }
        });
        eventBus.addListener((final ScreenEvent.KeyPressed.Post evt) -> {
            if (evt.getScreen() instanceof PackSelectionScreen screen) {
                PackActionsHandler.onKeyPressed$Post(screen.getMinecraft(),
                        screen,
                        evt.getKeyCode(),
                        evt.getScanCode(),
                        evt.getModifiers()
                );
            }
        });
        eventBus.addListener((final ClientTickEvent.Post evt) -> {
            PackActionsHandler.onClientTick$End(Minecraft.getInstance());
        });
    }
}
