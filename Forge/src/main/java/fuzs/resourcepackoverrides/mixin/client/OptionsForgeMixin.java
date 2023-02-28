package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Options.class)
abstract class OptionsForgeMixin {
    @Shadow
    public List<String> resourcePacks;
    @Shadow
    public List<String> incompatibleResourcePacks;
    @Unique
    private boolean resourcePackOverrides$wasEmpty;

    @Inject(method = "load(Z)V", at = @At("RETURN"), remap = false)
    private void load(boolean limited, CallbackInfo callback) {
        // Add built-in resource packs if they are enabled by default only if the options file is blank.
        if (this.resourcePacks.isEmpty()) {
            this.resourcePackOverrides$wasEmpty = true;
            List<String> defaultResourcePacks = ResourceOverridesManager.getDefaultResourcePacks(false);
            // If we provide an ordering for already present packs (from Forge) then apply our order instead of the existing one.
            this.resourcePacks.removeAll(defaultResourcePacks);
            this.resourcePacks.addAll(defaultResourcePacks);
        }
    }

    @Inject(method = "loadSelectedResourcePacks", at = @At("HEAD"))
    public void loadSelectedResourcePacks(PackRepository resourcePackList, CallbackInfo callback) {
        // We need to add incompatible packs that are enabled by default to the incompatible list, otherwise they are removed here.
        // This cannot be done earlier as the pack repository is still empty.
        if (this.resourcePackOverrides$wasEmpty) {
            this.resourcePackOverrides$wasEmpty = false;
            for (String s : ResourceOverridesManager.getDefaultResourcePacks(false)) {
                Pack pack = resourcePackList.getPack(s);
                if (pack == null && !s.startsWith("file/")) {
                    pack = resourcePackList.getPack("file/" + s);
                }
                if (pack != null && !pack.getCompatibility().isCompatible()) {
                    this.incompatibleResourcePacks.add(s);
                }
            }
        }
    }
}
