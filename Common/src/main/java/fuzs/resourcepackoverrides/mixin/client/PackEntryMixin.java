package fuzs.resourcepackoverrides.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.client.gui.screens.packs.PackAwareSelectionEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TransferableSelectionList.PackEntry.class)
abstract class PackEntryMixin extends ObjectSelectionList.Entry<TransferableSelectionList.PackEntry> {
    @Shadow
    @Final
    protected Screen screen;
    @Shadow
    @Final
    private PackSelectionModel.Entry pack;

    @Inject(method = "render", at = @At("TAIL"))
    public void render(PoseStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick, CallbackInfo callback) {
        if (isMouseOver && this.pack instanceof PackAwareSelectionEntry entry && ResourceOverridesManager.debugTooltips) {
            this.screen.renderTooltip(poseStack, new TextComponent(entry.getPackId()).withStyle(ChatFormatting.GRAY), mouseX, mouseY);
        }
    }
}
