package fuzs.resourcepackoverrides.fabric.services;

import com.google.common.base.Predicates;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.fabricmc.fabric.impl.resource.loader.FabricResourcePackProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;

import java.nio.file.Path;
import java.util.List;

public final class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public ModLoader getModLoader() {
        return ModLoader.FABRIC;
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isPackHidden(Pack pack) {
        // Fabric already has all the infrastructure for this flag implemented (which is quite a lot!), so we use it despite it being internal.
        return ((FabricResourcePackProfile) pack).fabric_isHidden();
    }

    @Override
    public void setPackHidden(Pack pack, boolean hidden) {
        // Fabric Api checks this using reference equality against an internally stored field when a pack is not supposed to be hidden.
        // We do not have access to that field, so we only support making the pack hidden, which is fine.
        if (hidden) {
            ((FabricResourcePackProfile) pack).fabric_setParentsPredicate(Predicates.alwaysTrue());
        }
    }

    @Override
    public Pack.Metadata createPackInfo(Component description, PackCompatibility compatibility, FeatureFlagSet features, List<String> overlays, boolean isHidden) {
        return new Pack.Metadata(description, compatibility, features, overlays);
    }
}
