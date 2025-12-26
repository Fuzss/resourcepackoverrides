package fuzs.resourcepackoverrides.services;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.List;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderLoader.load(ClientAbstractions.class);

    ModLoader getModLoader();

    Path getConfigDirectory();

    boolean isPackHidden(Pack pack);

    void setPackHidden(Pack pack, boolean isHidden);

    Pack.Metadata createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean isHidden);

    enum ModLoader {
        FABRIC, NEOFORGE
    }
}
