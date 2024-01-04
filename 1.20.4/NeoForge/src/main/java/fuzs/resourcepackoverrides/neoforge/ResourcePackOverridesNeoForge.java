package fuzs.resourcepackoverrides.neoforge;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.data.client.ModLanguageProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(ResourcePackOverrides.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcePackOverridesNeoForge {

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        evt.getGenerator().addProvider(true, new ModLanguageProvider(ResourcePackOverrides.MOD_ID, evt.getGenerator().getPackOutput()));
    }
}
