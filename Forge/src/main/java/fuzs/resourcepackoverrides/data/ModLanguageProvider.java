package fuzs.resourcepackoverrides.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("packAction.copyId.title", "Copying Pack Id...");
        this.add("packAction.copyId.description", "Continue hold %s to copy");
        this.add("packAction.copyId.success", "Copied pack id to clipboard!");
        this.add("packAction.reloadSettings.title", "Reloading Settings...");
        this.add("packAction.reloadSettings.description", "Continue hold %s to reload");
        this.add("packAction.reloadSettings.success", "Reloaded settings!");
        this.add("packAction.toggleDebug.title", "Toggling Pack Ids...");
        this.add("packAction.toggleDebug.description", "Continue hold %s to toggle");
        this.add("packAction.toggleDebug.success", "Toggled pack ids!");
    }
}
