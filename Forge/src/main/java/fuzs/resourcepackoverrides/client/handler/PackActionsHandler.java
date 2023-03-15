package fuzs.resourcepackoverrides.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.client.gui.components.HoverableAbstractSelectionList;
import fuzs.resourcepackoverrides.client.gui.screens.packs.PackAwareSelectionEntry;
import fuzs.resourcepackoverrides.mixin.client.accessor.PackEntryAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.gui.widget.list.ResourcePackList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Optional;

public class PackActionsHandler {
    private static final Int2ObjectMap<PackAction> PACK_ACTIONS = new Int2ObjectOpenHashMap<>();
    private static boolean debugTooltips;

    static {
        PACK_ACTIONS.put(67, new PackAction(new TranslationTextComponent("packAction.copyId.title"), new TranslationTextComponent("packAction.copyId.description", new StringTextComponent("C").withStyle(TextFormatting.BOLD)), new TranslationTextComponent("packAction.copyId.success")) {

            @Override
            boolean execute(Minecraft minecraft) {
                Optional<String> hoveredPackId = getHoveredPackId(minecraft.screen);
                hoveredPackId.ifPresent(minecraft.keyboardHandler::setClipboard);
                return hoveredPackId.isPresent();
            }
        });
        PACK_ACTIONS.put(68, new PackAction(new TranslationTextComponent("packAction.toggleDebug.title"), new TranslationTextComponent("packAction.toggleDebug.description", new StringTextComponent("D").withStyle(TextFormatting.BOLD)), new TranslationTextComponent("packAction.toggleDebug.success")) {

            @Override
            boolean execute(Minecraft minecraft) {
                debugTooltips = !debugTooltips;
                return true;
            }
        });
        PACK_ACTIONS.put(82, new PackAction(new TranslationTextComponent("packAction.reloadSettings.title"), new TranslationTextComponent("packAction.reloadSettings.description", new StringTextComponent("R").withStyle(TextFormatting.BOLD)), new TranslationTextComponent("packAction.reloadSettings.success")) {

            @Override
            boolean execute(Minecraft minecraft) {
                ResourceOverridesManager.load();
                if (minecraft.screen != null) {
                    minecraft.screen.init(minecraft, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
                }
                return true;
            }
        });
    }

    public static void onScreen$Render$Post(Screen screen, MatrixStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (debugTooltips) {
            getHoveredPackId(screen).map(StringTextComponent::new).ifPresent(component -> {
                screen.renderTooltip(poseStack, component, mouseX, mouseY);
            });
        }
    }

    private static Optional<String> getHoveredPackId(@Nullable Screen screen) {
        if (screen == null) return Optional.empty();
        for (IGuiEventListener guiEventListener : screen.children()) {
            if (guiEventListener instanceof AbstractList<?>) {
            AbstractList.AbstractListEntry<?> entry = ((HoverableAbstractSelectionList<?>) guiEventListener).resourcepackoverrides$getHovered();
                if (entry instanceof ResourcePackList.ResourcePackEntry) {
                    PackLoadingManager.IPack selectionEntry = ((PackEntryAccessor) entry).resourcepackoverrides$getPack();
                    if (selectionEntry instanceof PackAwareSelectionEntry) {
                        return Optional.of(((PackAwareSelectionEntry) selectionEntry).getPackId().replace("§", "\\u00A7"));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        PACK_ACTIONS.values().forEach(action -> action.tick(minecraft));
    }

    public static void onKeyPressed$Post(Screen screen, int keyCode, int scanCode, int modifiers) {
        PackAction packAction = PACK_ACTIONS.get(keyCode);
        if (packAction != null) packAction.update();
    }

    private static abstract class PackAction {
        private final ITextComponent title;
        private final ITextComponent description;
        private final ITextComponent success;
        @Nullable
        private TutorialToast toast;
        @Nullable
        private TutorialToast successToast;
        private int successTicks;
        private int pressTime;
        private int lastPressTime;
        private int decreaseTimeDelay;
        private boolean wasExecuted;

        public PackAction(ITextComponent title, ITextComponent description, ITextComponent success) {
            this.title = title;
            this.description = description;
            this.success = success;
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
                    this.toast = new TutorialToast(TutorialToast.Icons.MOVEMENT_KEYS, this.title, this.description, true);
                    minecraft.getToasts().addToast(this.toast);
                }
                if (this.pressTime < 20) {
                    this.toast.updateProgress(MathHelper.clamp(this.pressTime / 20.0F, 0.0F, 1.0F));
                } else if (!this.wasExecuted) {
                    if (this.execute(minecraft)) {
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

        abstract boolean execute(Minecraft minecraft);

        private void finish(Minecraft minecraft) {
            if (this.successToast != null) this.successToast.hide();
            this.successToast = new TutorialToast(TutorialToast.Icons.MOVEMENT_KEYS, this.title, this.success, true);
            minecraft.getToasts().addToast(this.successToast);
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
}
