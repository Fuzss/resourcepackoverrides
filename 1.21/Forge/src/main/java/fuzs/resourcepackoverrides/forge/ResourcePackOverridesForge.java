package fuzs.resourcepackoverrides.forge;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.bus.api.SubscribeEvent;

@Mod(ResourcePackOverrides.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcePackOverridesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        // NO-OP
    }
}
