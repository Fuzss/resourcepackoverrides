package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Shadow
    @Final
    private ResourcePackList resourcePackRepository;

    @Inject(method = "clearResourcePacksOnError", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourcePackList;setSelected(Ljava/util/Collection;)V", shift = At.Shift.AFTER))
    public void clearResourcePacksOnError(Throwable throwable, @Nullable ITextComponent errorMessage, CallbackInfo callback) {
        // Instead of setting an empty selection, set our default packs.
        // Default packs will return empty if this fails too often (more than 5 times by default).
        this.resourcePackRepository.setSelected(ResourceOverridesManager.getDefaultResourcePacks(true));
    }
}
