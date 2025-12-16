package fuzs.resourcepackoverrides.neoforge.mixin.client.accessor;

import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Pack.class)
public interface PackNeoForgeAccessor {

    @Accessor("hidden")
    @Mutable
    void resourcepackoverrides$setHidden(boolean hidden);
}
