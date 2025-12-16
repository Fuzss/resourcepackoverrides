package fuzs.resourcepackoverrides.neoforge.data.client;

import fuzs.resourcepackoverrides.client.handler.PackActionsHandler;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Objects;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(String modId, PackOutput packOutput) {
        super(packOutput, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(PackActionsHandler.COPY_ID_ACTION.title, "Copy Pack Id");
        this.add(PackActionsHandler.COPY_ID_ACTION.description, "Hold %s to copy");
        this.add(PackActionsHandler.COPY_ID_ACTION.success, "Copied!");
        this.add(PackActionsHandler.RELOAD_SETTINGS_ACTION.title, "Reload Settings");
        this.add(PackActionsHandler.RELOAD_SETTINGS_ACTION.description, "Hold %s to reload");
        this.add(PackActionsHandler.RELOAD_SETTINGS_ACTION.success, "Reloaded!");
        this.add(PackActionsHandler.TOGGLE_DEBUG_ACTION.title, "Toggle Pack Ids");
        this.add(PackActionsHandler.TOGGLE_DEBUG_ACTION.description, "Hold %s to toggle");
        this.add(PackActionsHandler.TOGGLE_DEBUG_ACTION.success, "Toggled!");
        this.add(PackActionsHandler.RESTORE_DEFAULTS_ACTION.title, "Restore Default Packs");
        this.add(PackActionsHandler.RESTORE_DEFAULTS_ACTION.description, "Hold %s to restore");
        this.add(PackActionsHandler.RESTORE_DEFAULTS_ACTION.success, "Restored!");
    }

    public void add(Component component, String value) {
        Objects.requireNonNull(component, "component is null");
        if (component.getContents() instanceof TranslatableContents contents) {
            this.add(contents.getKey(), value);
        } else {
            throw new IllegalArgumentException("Unsupported component: " + component);
        }
    }
}
