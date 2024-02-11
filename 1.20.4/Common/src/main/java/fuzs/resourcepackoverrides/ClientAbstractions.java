package fuzs.resourcepackoverrides;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.List;

public interface ClientAbstractions {

    @ExpectPlatform
    static Path getConfigDirectory() {
        throw new RuntimeException();
    }

    @ExpectPlatform
    static Font getScreenFont(Screen screen) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    static boolean isPackHidden(Pack pack) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    static Pack.Info createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean hidden) {
        throw new RuntimeException();
    }

    @ExpectPlatform
    static Pack finalizePack(Pack oldPack, Pack newPack) {
        throw new RuntimeException();
    }
}
