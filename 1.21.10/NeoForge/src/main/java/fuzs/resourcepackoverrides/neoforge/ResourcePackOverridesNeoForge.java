package fuzs.resourcepackoverrides.neoforge;

import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(ResourcePackOverrides.MOD_ID)
public class ResourcePackOverridesNeoForge {

    public ResourcePackOverridesNeoForge(ModContainer modContainer) {
        registerLoadingHandlers(modContainer.getEventBus());
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final GatherDataEvent.Client event) -> {
            event.getGenerator()
                    .addProvider(true,
                            PackMetadataGenerator.forFeaturePack(event.getGenerator().getPackOutput(),
                                    Component.literal(event.getModContainer().getModInfo().getDescription())));
        });
    }
}
