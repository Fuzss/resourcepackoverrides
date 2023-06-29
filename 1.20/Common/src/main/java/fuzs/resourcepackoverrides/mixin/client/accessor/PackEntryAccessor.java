package fuzs.resourcepackoverrides.mixin.client.accessor;

import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TransferableSelectionList.PackEntry.class)
public interface PackEntryAccessor {

    @Accessor("pack")
    PackSelectionModel.Entry resourcepackoverrides$getPack();
}
