package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.client.gui.screens.packs.PackAwareSelectionEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;

@Mixin(PackSelectionScreen.class)
abstract class PackSelectionScreenMixin extends Screen {

    protected PackSelectionScreenMixin(Component component) {
        super(component);
    }

    @ModifyVariable(method = "updateList", at = @At("HEAD"))
    private Stream<PackSelectionModel.Entry> updateList(Stream<PackSelectionModel.Entry> models) {
        // This also runs on the data packs screen, but that's fine since the entries are not wrapped there.
        return models.filter(entry -> {
            if (!(entry instanceof PackAwareSelectionEntry contextEntry)) {
                return true;
            }
            PackSelectionOverride override = ResourceOverridesManager.getOverride(contextEntry.getPackId());
            return override.hidden() == null || !override.hidden();
        });
    }
}
