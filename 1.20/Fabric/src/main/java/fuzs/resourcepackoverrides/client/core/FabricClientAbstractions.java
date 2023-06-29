package fuzs.resourcepackoverrides.client.core;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

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
}
