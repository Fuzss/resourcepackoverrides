package fuzs.resourcepackoverrides.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public class PackActionsHandler {
    private static final Int2ObjectMap<PackAction> PACK_ACTIONS = new Int2ObjectOpenHashMap<>();

    private static boolean debugTooltips;

    static {
        PACK_ACTIONS.put(InputConstants.KEY_C,
                new PackAction(Component.translatable("packAction.copyId.title"),
                        Component.translatable("packAction.copyId.description",
                                Component.literal("C").withStyle(ChatFormatting.BOLD)),
                        Component.translatable("packAction.copyId.success")) {

                    @Override
                    boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
                        MouseHandler mouseHandler = minecraft.mouseHandler;
                        Window window = minecraft.getWindow();
                        int mouseX = (int) (mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth());
                        int mouseY = (int) (mouseHandler.ypos() * window.getGuiScaledHeight() /
                                window.getScreenHeight());
                        Optional<String> hoveredPackId = getHoveredPackId(screen, mouseX, mouseY);
                        hoveredPackId.ifPresent(minecraft.keyboardHandler::setClipboard);
                        return hoveredPackId.isPresent();
                    }
                });
        PACK_ACTIONS.put(InputConstants.KEY_D,
                new PackAction(Component.translatable("packAction.toggleDebug.title"),
                        Component.translatable("packAction.toggleDebug.description",
                                Component.literal("D").withStyle(ChatFormatting.BOLD)),
                        Component.translatable("packAction.toggleDebug.success")) {

                    @Override
                    boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
                        debugTooltips = !debugTooltips;
                        return true;
                    }
                });
        PACK_ACTIONS.put(InputConstants.KEY_R,
                new PackAction(Component.translatable("packAction.reloadSettings.title"),
                        Component.translatable("packAction.reloadSettings.description",
                                Component.literal("R").withStyle(ChatFormatting.BOLD)),
                        Component.translatable("packAction.reloadSettings.success")) {

                    @Override
                    boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
                        ResourceOverridesManager.load();
                        screen.init(minecraft,
                                minecraft.getWindow().getGuiScaledWidth(),
                                minecraft.getWindow().getGuiScaledHeight());
                        return true;
                    }
                });
        PACK_ACTIONS.put(InputConstants.KEY_T,
                new PackAction(Component.translatable("packAction.restoreDefaults.title"),
                        Component.translatable("packAction.restoreDefaults.description",
                                Component.literal("T").withStyle(ChatFormatting.BOLD)),
                        Component.translatable("packAction.restoreDefaults.success")) {

                    @Override
                    boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
                        minecraft.getResourcePackRepository()
                                .setSelected(ResourceOverridesManager.getDefaultResourcePacks(true));
                        screen.model.selected.clear();
                        screen.model.selected.addAll(minecraft.getResourcePackRepository().getSelectedPacks());
                        Collections.reverse(screen.model.selected);
                        screen.init(minecraft,
                                minecraft.getWindow().getGuiScaledWidth(),
                                minecraft.getWindow().getGuiScaledHeight());
                        return true;
                    }
                });
    }

    public static void onScreen$Render$Post(Minecraft minecraft, PackSelectionScreen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (debugTooltips && screen.model.repository == minecraft.getResourcePackRepository()) {
            getHoveredPackId(screen, mouseX, mouseY).map(Component::literal).ifPresent(component -> {
                guiGraphics.renderTooltip(ClientAbstractions.INSTANCE.getScreenFont(screen), component, mouseX, mouseY);
            });
        }
    }

    private static Optional<String> getHoveredPackId(PackSelectionScreen screen, int mouseX, int mouseY) {
        if (screen == null) return Optional.empty();
        for (GuiEventListener guiEventListener : screen.children()) {
            if (guiEventListener instanceof TransferableSelectionList selectionList) {
                TransferableSelectionList.PackEntry hovered = null;
                for (TransferableSelectionList.PackEntry packEntry : selectionList.children()) {
                    if (packEntry.isMouseOver(mouseX, mouseY)) {
                        hovered = packEntry;
                        break;
                    }
                }
                if (hovered != null) {
                    return Optional.of(hovered.getPackId().replace("§", "\\u00A7"));
                }
            }
        }
        return Optional.empty();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        PACK_ACTIONS.values().forEach(action -> action.tick(minecraft));
    }

    public static void onKeyPressed$Post(Minecraft minecraft, PackSelectionScreen screen, int keyCode, int scanCode, int modifiers) {
        if (screen.model.repository == minecraft.getResourcePackRepository()) {
            PackAction packAction = PACK_ACTIONS.get(keyCode);
            if (packAction != null) packAction.update();
        }
    }

    private static abstract class PackAction {
        private final Component title;
        private final Component description;
        private final Component success;
        @Nullable
        private TutorialToast toast;
        @Nullable
        private TutorialToast successToast;
        private int successTicks;
        private int pressTime;
        private int lastPressTime;
        private int decreaseTimeDelay;
        private boolean wasExecuted;

        public PackAction(Component title, Component description, Component success) {
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
                    this.toast = new TutorialToast(minecraft.font,
                            TutorialToast.Icons.MOVEMENT_KEYS,
                            this.title,
                            this.description,
                            true);
                    minecraft.getToastManager().addToast(this.toast);
                }
                if (this.pressTime < 20) {
                    this.toast.updateProgress(Mth.clamp(this.pressTime / 20.0F, 0.0F, 1.0F));
                } else if (!this.wasExecuted && minecraft.screen instanceof PackSelectionScreen screen &&
                        screen.model.repository == minecraft.getResourcePackRepository()) {
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
}
