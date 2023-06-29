package fuzs.resourcepackoverrides.client.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

import java.nio.file.Path;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    Path getConfigDirectory();

    Font getScreenFont(Screen screen);
}
