package fuzs.resourcepackoverrides.neoforge.services;

import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.List;

public class NeoForgeClientAbstractions implements ClientAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isPackHidden(Pack pack) {
        return pack.isHidden();
    }

    @Override
    public Pack.Metadata createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean hidden) {
        return new Pack.Metadata(description, compatibility, features, overlays, hidden);
    }
}
