package fuzs.resourcepackoverrides.fabric.client.core.fabric;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.List;

public class ClientAbstractionsImpl {

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static Font getScreenFont(Screen screen) {
        return Screens.getTextRenderer(screen);
    }

    public static boolean isPackHidden(Pack pack) {
        return false;
    }

    public static Pack.Info createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean hidden) {
        return new Pack.Info(description, compatibility, features, overlays);
    }
}
