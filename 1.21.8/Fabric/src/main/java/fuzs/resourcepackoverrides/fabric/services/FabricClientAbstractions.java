package fuzs.resourcepackoverrides.fabric.services;

import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.List;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isPackHidden(Pack pack) {
        return false;
    }

    @Override
    public Pack.Metadata createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean hidden) {
        return new Pack.Metadata(description, compatibility, features, overlays);
    }
}
