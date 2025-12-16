package fuzs.resourcepackoverrides.neoforge.data.client;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(String modId, PackOutput packOutput) {
        super(packOutput, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("packAction.copyId.title", "Copy Pack Id");
        this.add("packAction.copyId.description", "Hold %s to copy");
        this.add("packAction.copyId.success", "Copied!");
        this.add("packAction.reloadSettings.title", "Reload Settings");
        this.add("packAction.reloadSettings.description", "Hold %s to reload");
        this.add("packAction.reloadSettings.success", "Reloaded!");
        this.add("packAction.toggleDebug.title", "Toggle Pack Ids");
        this.add("packAction.toggleDebug.description", "Hold %s to toggle");
        this.add("packAction.toggleDebug.success", "Toggled!");
        this.add("packAction.restoreDefaults.title", "Restore Default Packs");
        this.add("packAction.restoreDefaults.description", "Hold %s to restore");
        this.add("packAction.restoreDefaults.success", "Restored!");
    }
}
