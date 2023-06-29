package fuzs.resourcepackoverrides.client.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
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
}
