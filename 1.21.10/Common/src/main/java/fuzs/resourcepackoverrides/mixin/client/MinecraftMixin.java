package fuzs.resourcepackoverrides.mixin.client;

import fuzs.resourcepackoverrides.config.ResourceOverridesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
abstract class MinecraftMixin {
    @Shadow
    @Final
    private PackRepository resourcePackRepository;

    @Inject(method = "clearResourcePacksOnError",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/packs/repository/PackRepository;setSelected(Ljava/util/Collection;)V",
                     shift = At.Shift.AFTER))
    public void clearResourcePacksOnError(CallbackInfo callback) {
        // Instead of setting an empty selection, set our default packs.
        // Default packs will return empty if this fails too often (more than 5 times by default).
        this.resourcePackRepository.setSelected(ResourceOverridesManager.getDefaultResourcePacks(true));
    }
}
