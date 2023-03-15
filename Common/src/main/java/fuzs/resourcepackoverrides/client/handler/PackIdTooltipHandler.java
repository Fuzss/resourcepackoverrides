package fuzs.resourcepackoverrides.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.client.gui.screens.packs.PackAwareSelectionEntry;
import fuzs.resourcepackoverrides.mixin.client.accessor.AbstractSelectionListAccessor;
import fuzs.resourcepackoverrides.mixin.client.accessor.PackEntryAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PackIdTooltipHandler {
    private static boolean debugTooltips;
    private static final Int2ObjectMap<PackSelectionScreenAction> ACTIONS = new Int2ObjectOpenHashMap<>();

    static {
        ACTIONS.put(InputConstants.KEY_C, new PackSelectionScreenAction(Component.translatable("packAction.copyId.title"), Component.translatable("packAction.copyId.description", Component.literal("C").withStyle(ChatFormatting.BOLD)), Component.translatable("packAction.copyId.success")) {

            @Override
            void execute(Minecraft minecraft) {
                getHoveredPackId(minecraft.screen).ifPresent(minecraft.keyboardHandler::setClipboard);
            }
        });
        ACTIONS.put(InputConstants.KEY_D, new PackSelectionScreenAction(Component.translatable("packAction.toggleDebug.title"), Component.translatable("packAction.toggleDebug.description", Component.literal("D").withStyle(ChatFormatting.BOLD)), Component.translatable("packAction.toggleDebug.success")) {

            @Override
            void execute(Minecraft minecraft) {
                debugTooltips = !debugTooltips;
            }
        });
        ACTIONS.put(InputConstants.KEY_R, new PackSelectionScreenAction(Component.translatable("packAction.reloadSettings.title"), Component.translatable("packAction.reloadSettings.description", Component.literal("R").withStyle(ChatFormatting.BOLD)), Component.translatable("packAction.reloadSettings.success")) {

            @Override
            void execute(Minecraft minecraft) {
                ResourceOverridesManager.load();
            }
        });
    }

    public static void onScreen$Render$Post(Screen screen, PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (debugTooltips) {
            getHoveredPackId(screen).map(Component::literal).ifPresent(component -> {
                screen.renderTooltip(poseStack, component, mouseX, mouseY);
            });
        }
    }

    private static Optional<String> getHoveredPackId(@Nullable Screen screen) {
        if (screen == null) return Optional.empty();
        for (GuiEventListener guiEventListener : screen.children()) {
            if (guiEventListener instanceof ObjectSelectionList<?> selectionList) {
                if (((AbstractSelectionListAccessor<?>) selectionList).resourcepackoverrides$callGetHovered() instanceof TransferableSelectionList.PackEntry entry) {
                    if (((PackEntryAccessor) entry).resourcepackoverrides$getPack() instanceof PackAwareSelectionEntry selectionEntry) {
                        return Optional.of(selectionEntry.getPackId());
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        ACTIONS.values().forEach(action -> action.tick(minecraft));
    }

    public static void onKeyPressed$Post(Screen screen, int keyCode, int scanCode, int modifiers) {
        PackSelectionScreenAction packSelectionScreenAction = ACTIONS.get(keyCode);
        if (packSelectionScreenAction != null) packSelectionScreenAction.update();
    }

    private static abstract class PackSelectionScreenAction {
        private final Component title;
        private final Component description;
        private final Component success;
        @Nullable
        private TutorialToast toast;
        private int pressTime;
        private int lastPressTime;
        private int decreaseTimeDelay;
        private boolean wasExecuted;

        public PackSelectionScreenAction(Component title, Component description, Component success) {
            this.title = title;
            this.description = description;
            this.success = success;
        }

        public void tick(Minecraft minecraft) {
            if (this.pressTime == this.lastPressTime && this.pressTime > 0 && --this.decreaseTimeDelay < 0) {
                if (this.wasExecuted) {
                    this.reset();
                } else {
                    this.pressTime--;
                }
            }
            this.lastPressTime = this.pressTime;
            if (this.pressTime > 0) {
                if (this.toast == null) {
                    this.toast = new TutorialToast(TutorialToast.Icons.MOVEMENT_KEYS, this.title, this.description, true);
                    minecraft.getToasts().addToast(this.toast);
                }
                if (this.pressTime < 20) {
                    this.toast.updateProgress(Mth.clamp(this.pressTime / 20.0F, 0.0F, 1.0F));
                } else if (!this.wasExecuted) {
                    this.execute(minecraft);
                    this.wasExecuted = true;
                    this.toast.updateProgress(1.0F);
                    // SystemToastIds is an enum, but SystemToastIds#TUTORIAL_HINT is unused, also just responsible for controlling amount of display ticks
                    SystemToast.addOrUpdate(minecraft.getToasts(), SystemToast.SystemToastIds.TUTORIAL_HINT, this.success, null);
                }
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

        abstract void execute(Minecraft minecraft);

        public void update() {
            this.pressTime++;
            this.resetDelay();
        }

        public void resetDelay() {
            this.decreaseTimeDelay = 5;
        }
    }
}
