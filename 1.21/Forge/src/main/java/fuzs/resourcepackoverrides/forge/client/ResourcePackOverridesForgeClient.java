package fuzs.resourcepackoverrides.forge.client;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ResourcePackOverrides.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ResourcePackOverridesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerEventHandlers(MinecraftForge.EVENT_BUS);
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
        eventBus.addListener((final TickEvent.ClientTickEvent.Post evt) -> {
            PackActionsHandler.onClientTick$End(Minecraft.getInstance());
        });
    }
}
