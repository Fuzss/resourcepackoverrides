package fuzs.resourcepackoverrides.forge;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.List;

public class ClientAbstractionsImpl {

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Font getScreenFont(Screen screen) {
        return screen.getMinecraft().font;
    }

    public static boolean isPackHidden(Pack pack) {
        return pack.isHidden();
    }

    public static Pack.Info createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean hidden) {
        return new Pack.Info(description, compatibility, features, overlays, hidden);
    }

    public static Pack finalizePack(Pack oldPack, Pack newPack) {
        return newPack;
    }
}
