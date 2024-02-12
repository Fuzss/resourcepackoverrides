package fuzs.resourcepackoverrides.mixin.client.accessor;

import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PackSelectionModel.class)
public interface PackSelectionModelAccessor {

    @Accessor("repository")
    PackRepository resourcepackoverrides$getRepository();
}
