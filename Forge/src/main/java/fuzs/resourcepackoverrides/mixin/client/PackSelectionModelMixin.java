package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.gui.screens.packs.ForwardingPackSelectionModelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackLoadingManager.class)
abstract class PackSelectionModelMixin {
    @Shadow
    @Final
    private ResourcePackList repository;

    @Inject(method = {"lambda$getSelected$1", "lambda$getUnselected$0", "func_238866_a_", "func_238870_b_"}, at = @At("TAIL"), cancellable = true, remap = false)
    public void getSelected(ResourcePackInfo pack, CallbackInfoReturnable<PackLoadingManager.IPack> callback) {
        // Wrap only on resource pack selection screen, we don't want to mess with data packs.
        if (this.repository != Minecraft.getInstance().getResourcePackRepository()) return;
        callback.setReturnValue(new ForwardingPackSelectionModelEntry(pack, callback.getReturnValue()));
    }
}
