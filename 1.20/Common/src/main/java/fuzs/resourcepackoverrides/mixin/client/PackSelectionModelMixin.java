package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PackSelectionModel.class)
abstract class PackSelectionModelMixin {
    @Shadow
    @Final
    private PackRepository repository;
    @Shadow
    @Final
    List<Pack> selected;
    @Shadow
    @Final
    List<Pack> unselected;

    @Inject(method = "findNewPacks", at = @At("TAIL"))
    public void findNewPacks(CallbackInfo callback) {
        // Wrap only on resource pack selection screen, we don't want to mess with data packs.
        if (this.repository != Minecraft.getInstance().getResourcePackRepository()) return;
        this.selected.removeIf(pack -> {
            PackSelectionOverride override = ResourceOverridesManager.getOverride(pack.getId());
            return override.hidden() != null && override.hidden();
        });
        this.unselected.removeIf(pack -> {
            PackSelectionOverride override = ResourceOverridesManager.getOverride(pack.getId());
            return override.hidden() != null && override.hidden();
        });
    }
}
