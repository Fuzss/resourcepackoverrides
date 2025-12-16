package fuzs.resourcepackoverrides.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import fuzs.resourcepackoverrides.config.ResourceOverridesManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackActionsHandler {
    public static final PackAction COPY_ID_ACTION = new PackAction(InputConstants.KEY_C, "copy_id") {
        @Override
        boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
            MouseHandler mouseHandler = minecraft.mouseHandler;
            Window window = minecraft.getWindow();
            int mouseX = (int) (mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth());
            int mouseY = (int) (mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight());
            Optional<String> hoveredPackId = getHoveredPackId(screen, mouseX, mouseY);
            hoveredPackId.ifPresent(minecraft.keyboardHandler::setClipboard);
            return hoveredPackId.isPresent();
        }
    };
    public static final PackAction TOGGLE_DEBUG_ACTION = new PackAction(InputConstants.KEY_D, "toggle_debug") {
        @Override
        boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
            debugTooltips = !debugTooltips;
            return true;
        }
    };
    public static final PackAction RELOAD_SETTINGS_ACTION = new PackAction(InputConstants.KEY_R, "reload_settings") {
        @Override
        boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
            ResourceOverridesManager.clearGlobalPackConfig();
            screen.init(minecraft,
                    minecraft.getWindow().getGuiScaledWidth(),
                    minecraft.getWindow().getGuiScaledHeight());
            return true;
        }
    };
    public static final PackAction RESTORE_DEFAULTS_ACTION = new PackAction(InputConstants.KEY_T, "restore_defaults") {
        @Override
        boolean execute(Minecraft minecraft, PackSelectionScreen screen) {
            minecraft.getResourcePackRepository().setSelected(ResourceOverridesManager.getDefaultResourcePacks(true));
            screen.model.selected.clear();
            screen.model.selected.addAll(minecraft.getResourcePackRepository().getSelectedPacks());
            Collections.reverse(screen.model.selected);
            screen.init(minecraft,
                    minecraft.getWindow().getGuiScaledWidth(),
                    minecraft.getWindow().getGuiScaledHeight());
            return true;
        }
    };
    private static final Int2ObjectMap<PackAction> PACK_ACTIONS = Stream.of(COPY_ID_ACTION,
                    TOGGLE_DEBUG_ACTION,
                    RELOAD_SETTINGS_ACTION,
                    RESTORE_DEFAULTS_ACTION)
            .collect(Collectors.toMap(PackAction::getKeyCode,
                    Function.identity(),
                    (PackAction o1, PackAction o2) -> o2,
                    Int2ObjectOpenHashMap::new));

    private static boolean debugTooltips;

    public static void onScreen$Render$Post(Minecraft minecraft, PackSelectionScreen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (debugTooltips && screen.model.repository == minecraft.getResourcePackRepository()) {
            getHoveredPackId(screen, mouseX, mouseY).map(Component::literal).ifPresent(component -> {
                guiGraphics.setTooltipForNextFrame(screen.getFont(), component, mouseX, mouseY);
                // hack fix, as this does not render otherwise for some reason
                guiGraphics.renderDeferredElements();
            });
        }
    }

    private static Optional<String> getHoveredPackId(PackSelectionScreen screen, int mouseX, int mouseY) {
        if (screen == null) return Optional.empty();
        for (GuiEventListener guiEventListener : screen.children()) {
            if (guiEventListener instanceof TransferableSelectionList selectionList) {
                TransferableSelectionList.Entry hoveredEntry = null;
                for (TransferableSelectionList.Entry packEntry : selectionList.children()) {
                    if (packEntry.isMouseOver(mouseX, mouseY)) {
                        hoveredEntry = packEntry;
                        break;
                    }
                }
                if (hoveredEntry != null) {
                    return Optional.of(hoveredEntry.getPackId().replace("§", "\\u00A7"));
                }
            }
        }
        return Optional.empty();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        PACK_ACTIONS.values().forEach((PackAction action) -> {
            action.tick(minecraft);
        });
    }

    public static void onKeyPressed$Post(Minecraft minecraft, PackSelectionScreen screen, KeyEvent keyEvent) {
        if (screen.model.repository == minecraft.getResourcePackRepository()) {
            PackAction packAction = PACK_ACTIONS.get(keyEvent.key());
            if (packAction != null) {
                packAction.update();
            }
        }
    }
}
