package fuzs.resourcepackoverrides.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import fuzs.resourcepackoverrides.client.gui.components.HoverableAbstractSelectionList;
import net.minecraft.client.gui.FocusableGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.widget.list.AbstractList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractList.class)
abstract class AbstractSelectionListMixin<E extends AbstractList.AbstractListEntry<E>> extends FocusableGui implements IRenderable, HoverableAbstractSelectionList<E> {
    @Nullable
    private E resourcepackoverrides$hovered;

    @Nullable
    @Override
    public E resourcepackoverrides$getHovered() {
        return this.resourcepackoverrides$hovered;
    }

    @Shadow
    @Nullable
    protected final E getEntryAtPosition(double mouseX, double mouseY) {
        throw new RuntimeException();
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo callback) {
        this.resourcepackoverrides$hovered = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
    }
}
