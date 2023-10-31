package fuzs.resourcepackoverrides.client.core;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Font getScreenFont(Screen screen) {
        return Screens.getTextRenderer(screen);
    }

    @Override
    public boolean isPackHidden(Pack pack) {
        return false;
    }

    @Override
    public Pack.Info createPackInfo(Component description, int packVersion, FeatureFlagSet features, boolean hidden) {
        return new Pack.Info(description, packVersion, features);
    }
}
