package fuzs.resourcepackoverrides.neoforge.client;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(modid = ResourcePackOverrides.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ResourcePackOverridesNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {
        NeoForge.EVENT_BUS.addListener((final ScreenEvent.Render.Post evt) -> {
            if (evt.getScreen() instanceof PackSelectionScreen screen) {
                PackActionsHandler.onScreen$Render$Post(screen.getMinecraft(), screen, evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY(), evt.getPartialTick());
            }
        });
        NeoForge.EVENT_BUS.addListener((final ScreenEvent.KeyPressed.Post evt) -> {
            if (evt.getScreen() instanceof PackSelectionScreen screen) {
                PackActionsHandler.onKeyPressed$Post(screen.getMinecraft(), screen, evt.getKeyCode(), evt.getScanCode(), evt.getModifiers());
            }
        });
        NeoForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) PackActionsHandler.onClientTick$End(Minecraft.getInstance());
        });
    }
}
