package fuzs.resourcepackoverrides.client;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.PackScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ResourcePackOverrides.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ResourcePackOverridesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final GuiScreenEvent.DrawScreenEvent.Post evt) -> {
            if (evt.getGui() instanceof PackScreen) {
                PackActionsHandler.onScreen$Render$Post(evt.getGui(), evt.getMatrixStack(), evt.getMouseX(), evt.getMouseY(), evt.getRenderPartialTicks());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final GuiScreenEvent.KeyboardKeyPressedEvent.Post evt) -> {
            if (evt.getGui() instanceof PackScreen) {
                PackActionsHandler.onKeyPressed$Post(evt.getGui(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) PackActionsHandler.onClientTick$End(Minecraft.getInstance());
        });
    }
}
