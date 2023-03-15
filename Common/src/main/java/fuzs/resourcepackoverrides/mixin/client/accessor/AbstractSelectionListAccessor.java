package fuzs.resourcepackoverrides.mixin.client.accessor;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSelectionList.class)
public interface AbstractSelectionListAccessor<E extends AbstractSelectionList.Entry<E>> {

    @Nullable
    @Invoker("getHovered")
    E resourcepackoverrides$callGetHovered();
}
