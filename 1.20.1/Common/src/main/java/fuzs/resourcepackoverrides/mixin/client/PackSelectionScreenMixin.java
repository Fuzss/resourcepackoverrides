package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.mixin.client.accessor.PackSelectionModelAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.stream.Stream;

@Mixin(PackSelectionScreen.class)
abstract class PackSelectionScreenMixin extends Screen {
    @Shadow
    @Final
    private PackSelectionModel model;

    protected PackSelectionScreenMixin(Component title) {
        super(title);
    }

    @ModifyVariable(method = "updateList", at = @At("HEAD"), argsOnly = true)
    private Stream<PackSelectionModel.Entry> updateList(Stream<PackSelectionModel.Entry> models) {
        if (((PackSelectionModelAccessor) this.model).resourcepackoverrides$getRepository() != Minecraft.getInstance().getResourcePackRepository()) {
            return models;
        }
        return models.filter(pack -> {
            PackSelectionOverride override = ResourceOverridesManager.getOverride(pack.getId());
            return override.hidden() == null || !override.hidden();
        });
    }
}
