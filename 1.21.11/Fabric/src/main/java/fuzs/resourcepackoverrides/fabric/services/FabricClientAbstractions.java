package fuzs.resourcepackoverrides.fabric.services;

import com.google.common.base.Predicates;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.fabricmc.fabric.impl.resource.pack.FabricPack;
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
        return ((FabricPack) pack).fabric$isHidden();
    }

    @Override
    public void setPackHidden(Pack pack, boolean isHidden) {
        if (isHidden && !this.isPackHidden(pack)) {
            // Fabric Api checks this using reference equality against an internally stored field when a pack is not supposed to be hidden.
            // We do not have access to that field, so we only support making the pack hidden, which is fine.
            // Should be enough to set this predicate based on required, as there is not really another way to change the state between enabled / disabled.
            ((FabricPack) pack).fabric$setParentsPredicate(
                    pack.isRequired() ? Predicates.alwaysTrue() : Predicates.alwaysFalse());
        }
    }

    @Override
    public Pack.Metadata createPackInfo(Component descriptionComponent, PackCompatibility packCompatibility, FeatureFlagSet featureFlagSet, List<String> overlays, boolean isHidden) {
        return new Pack.Metadata(descriptionComponent, packCompatibility, featureFlagSet, overlays);
    }
}
