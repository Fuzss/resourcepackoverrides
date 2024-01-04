package fuzs.resourcepackoverrides.client.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Font getScreenFont(Screen screen) {
        return screen.getMinecraft().font;
    }

    @Override
    public boolean isPackHidden(Pack pack) {
        return pack.isHidden();
    }

    @Override
    public Pack.Info createPackInfo(Component description, int packVersion, FeatureFlagSet features, boolean hidden) {
        return new Pack.Info(description, packVersion, packVersion, features, hidden);
    }
}
