package fuzs.resourcepackoverrides.client.core;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    Path getConfigDirectory();

    Font getScreenFont(Screen screen);

    boolean isPackHidden(Pack pack);

    Pack.Info createPackInfo(Component description, int packVersion, FeatureFlagSet features, boolean hidden);
}
