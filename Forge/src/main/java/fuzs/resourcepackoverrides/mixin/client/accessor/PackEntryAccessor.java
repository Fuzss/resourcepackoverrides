package fuzs.resourcepackoverrides.mixin.client.accessor;

import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.client.gui.widget.list.ResourcePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResourcePackList.ResourcePackEntry.class)
public interface PackEntryAccessor {

    @Accessor("pack")
    PackLoadingManager.IPack resourcepackoverrides$getPack();
}
