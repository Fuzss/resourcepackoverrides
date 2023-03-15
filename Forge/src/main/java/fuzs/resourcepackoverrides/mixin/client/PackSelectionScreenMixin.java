package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.client.gui.screens.packs.PackAwareSelectionEntry;
import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.client.gui.screen.PackScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;

@Mixin(PackScreen.class)
abstract class PackSelectionScreenMixin extends Screen {

    protected PackSelectionScreenMixin(ITextComponent component) {
        super(component);
    }

    @ModifyVariable(method = "updateList", at = @At("HEAD"))
    private Stream<PackLoadingManager.IPack> updateList(Stream<PackLoadingManager.IPack> models) {
        // This also runs on the data packs screen, but that's fine since the entries are not wrapped there.
        return models.filter(entry -> {
            if (!(entry instanceof PackAwareSelectionEntry)) {
                return true;
            }
            PackSelectionOverride override = ResourceOverridesManager.getOverride(((PackAwareSelectionEntry) entry).getPackId());
            return override.hidden() == null || !override.hidden();
        });
    }
}
