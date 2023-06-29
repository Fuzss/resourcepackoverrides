package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.gui.screens.packs.ForwardingPackSelectionModelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackSelectionModel.class)
abstract class PackSelectionModelMixin {
    @Shadow
    @Final
    private PackRepository repository;

    @Inject(method = {"method_29644", "method_29640", "lambda$getSelected$1", "lambda$getUnselected$0", "m_99914_", "m_99919_"}, at = @At("TAIL"), cancellable = true, remap = false)
    public void getSelected(Pack pack, CallbackInfoReturnable<PackSelectionModel.Entry> callback) {
        // Wrap only on resource pack selection screen, we don't want to mess with data packs.
        if (this.repository != Minecraft.getInstance().getResourcePackRepository()) return;
        callback.setReturnValue(new ForwardingPackSelectionModelEntry(pack, callback.getReturnValue()));
    }
}
