package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.config.ResourceOverridesManager;
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

// Run after Fabric Api, or we might break mod resource packs being added.
@Mixin(value = Options.class, priority = 2000)
abstract class OptionsMixin {
    @Shadow
    public List<String> resourcePacks;
    @Shadow
    public List<String> incompatibleResourcePacks;
    @Unique
    private boolean resourcePackOverrides$wasEmpty;

    @Inject(method = "load", at = @At(value = "HEAD"))
    private void load$0(CallbackInfo callback) {
        // Runs before options.txt file is read, value will remain if the file is not present and needs to generate first.
        this.resourcePackOverrides$wasEmpty = this.resourcePacks.isEmpty();
    }

    @Inject(method = "load",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/KeyMapping;resetMapping()V",
                     shift = At.Shift.AFTER))
    private void load$1(CallbackInfo callback) {
        // Runs after options.txt file is read, so will be skipped when it has not been generated yet.
        this.resourcePackOverrides$wasEmpty = this.resourcePacks.isEmpty();
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void load$2(CallbackInfo callback) {
        // Add built-in resource packs if they are enabled by default only if the options file is blank.
        // Don't check on resource packs list being empty directly as it has already been altered by Fabric Api.
        if (this.resourcePackOverrides$wasEmpty) {
            List<String> defaultResourcePacks = ResourceOverridesManager.getDefaultResourcePacks(false);
            // If we provide an ordering for already present packs (from Fabric Api) then apply our order instead of the existing one.
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
            for (String packName : ResourceOverridesManager.getDefaultResourcePacks(false)) {
                Pack pack = resourcePackList.getPack(packName);
                if (pack == null && !packName.startsWith("file/")) {
                    pack = resourcePackList.getPack("file/" + packName);
                }

                if (pack != null && !pack.getCompatibility().isCompatible()) {
                    this.incompatibleResourcePacks.add(packName);
                }
            }
        }
    }
}
