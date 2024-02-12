package fuzs.resourcepackoverrides.mixin.client;

import com.google.common.collect.Lists;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.client.Options;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Options.class)
abstract class OptionsMixin {
    @Shadow
    public List<String> resourcePacks;

    @ModifyVariable(method = "updateResourcePacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;"), ordinal = 0)
    public List<String> updateResourcePacks(List<String> oldPacks, PackRepository p_275268_) {
        ResourcePackOverrides.LOGGER.info("old packs {}", oldPacks);
        ResourcePackOverrides.LOGGER.info("new packs {}", this.resourcePacks);
        return oldPacks;
    }
}
