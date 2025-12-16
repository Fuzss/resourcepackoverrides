package fuzs.resourcepackoverrides.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public abstract class PackAction {
    private final int keyCode;
    public final Component title;
    public final Component description;
    public final Component success;
    @Nullable
    private TutorialToast toast;
    @Nullable
    private TutorialToast successToast;
    private int successTicks;
    private int pressTime;
    private int lastPressTime;
    private int decreaseTimeDelay;
    private boolean wasExecuted;

    public PackAction(int keyCode, String name) {
        this(keyCode, ResourcePackOverrides.id(name));
    }

    public PackAction(int keyCode, ResourceLocation resourceLocation) {
        this(keyCode,
                Component.translatable(resourceLocation.toLanguageKey("pack_action", "title")),
                Component.translatable(resourceLocation.toLanguageKey("pack_action", "description"),
                        InputConstants.Type.KEYSYM.getOrCreate(keyCode)
                                .getDisplayName()
                                .copy()
                                .withStyle(ChatFormatting.BOLD)),
                Component.translatable(resourceLocation.toLanguageKey("pack_action", "success")));
    }

    public PackAction(int keyCode, Component title, Component description, Component success) {
        this.keyCode = keyCode;
        this.title = title;
        this.description = description;
        this.success = success;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void tick(Minecraft minecraft) {
        if (this.pressTime == this.lastPressTime && this.pressTime > 0) {
            if (--this.decreaseTimeDelay < 0) {
                if (this.wasExecuted) {
                    this.reset();
                } else {
                    this.pressTime--;
                }
            }
        }
        this.lastPressTime = this.pressTime;
        if (this.pressTime > 0) {
            if (this.toast == null) {
                this.toast = new TutorialToast(minecraft.font,
                        TutorialToast.Icons.MOVEMENT_KEYS,
                        this.title,
                        this.description,
                        true);
                minecraft.getToastManager().addToast(this.toast);
            }

            if (this.pressTime < 20) {
                this.toast.updateProgress(Mth.clamp(this.pressTime / 20.0F, 0.0F, 1.0F));
            } else if (!this.wasExecuted && minecraft.screen instanceof PackSelectionScreen screen
                    && screen.model.repository == minecraft.getResourcePackRepository()) {
                if (this.execute(minecraft, screen)) {
                    this.finish(minecraft);
                }
                this.wasExecuted = true;
                this.toast.updateProgress(1.0F);
            }
        } else {
            this.reset();
        }

        if (this.successTicks > 0) {
            this.successTicks--;
            this.successToast.updateProgress(this.successTicks / 80.0F);
        } else if (this.successToast != null) {
            this.successToast.hide();
            this.successToast = null;
        }
    }

    private void reset() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }

        this.pressTime = this.lastPressTime = 0;
        this.wasExecuted = false;
    }

    abstract boolean execute(Minecraft minecraft, PackSelectionScreen screen);

    private void finish(Minecraft minecraft) {
        if (this.successToast != null) this.successToast.hide();
        this.successToast = new TutorialToast(minecraft.font,
                TutorialToast.Icons.MOVEMENT_KEYS,
                this.title,
                this.success,
                true);
        minecraft.getToastManager().addToast(this.successToast);
        this.successTicks = 80;
        this.successToast.updateProgress(1.0F);
    }

    public void update() {
        this.pressTime++;
        this.resetDelay();
    }

    public void resetDelay() {
        // this high of a delay is necessary as for some reason the key takes a few ticks until it reports as pressed again after the initial press
        this.decreaseTimeDelay = 10;
    }
}
