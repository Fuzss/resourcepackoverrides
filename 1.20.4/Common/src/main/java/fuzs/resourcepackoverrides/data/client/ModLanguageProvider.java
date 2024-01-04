package fuzs.resourcepackoverrides.data.client;

import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(String modId, PackOutput packOutput) {
        super(modId, packOutput);
    }

    @Override
    protected void addTranslations(TranslationBuilder builder) {
        builder.add("packAction.copyId.title", "Copy Pack Id");
        builder.add("packAction.copyId.description", "Hold %s to copy");
        builder.add("packAction.copyId.success", "Copied!");
        builder.add("packAction.reloadSettings.title", "Reload Settings");
        builder.add("packAction.reloadSettings.description", "Hold %s to reload");
        builder.add("packAction.reloadSettings.success", "Reloaded!");
        builder.add("packAction.toggleDebug.title", "Toggle Pack Ids");
        builder.add("packAction.toggleDebug.description", "Hold %s to toggle");
        builder.add("packAction.toggleDebug.success", "Toggled!");
    }
}
