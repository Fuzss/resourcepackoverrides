package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.config.ResourceOverridesManager;
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

    @ModifyVariable(method = "filterEntries", at = @At("HEAD"), argsOnly = true)
    private Stream<PackSelectionModel.Entry> filterEntries(Stream<PackSelectionModel.Entry> stream) {
        if (this.model.repository != Minecraft.getInstance().getResourcePackRepository()) {
            return stream;
        } else {
            return stream.filter((PackSelectionModel.Entry entry) -> {
                return ResourceOverridesManager.getOverride(entry.getId()).notHidden();
            });
        }
    }
}
