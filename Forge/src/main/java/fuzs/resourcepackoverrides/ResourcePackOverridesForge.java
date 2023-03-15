package fuzs.resourcepackoverrides;

import fuzs.resourcepackoverrides.data.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod(ResourcePackOverrides.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcePackOverridesForge {

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(new ModLanguageProvider(dataGenerator, ResourcePackOverrides.MOD_ID));
    }
}
