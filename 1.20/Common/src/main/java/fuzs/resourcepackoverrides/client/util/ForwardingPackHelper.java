package fuzs.resourcepackoverrides.client.util;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;

public class ForwardingPackHelper {

    public static Pack copy(Pack pack) {
        PackSelectionOverride override = ResourceOverridesManager.getOverride(pack.getId());
        Component title = override.title() != null ? override.title() : pack.getTitle();
        Component description = override.description() != null ? override.description() : pack.getDescription();
        PackCompatibility compatibility = override.compatibility() != null ? override.compatibility() : pack.getCompatibility();
        Boolean required = override.required() != null ? override.required() : Boolean.valueOf(pack.isRequired());
        Boolean fixedPosition = override.fixedPosition() != null ? override.fixedPosition() : Boolean.valueOf(pack.isFixedPosition());
        Pack.Position defaultPosition = override.defaultPosition() != null ? override.defaultPosition() : pack.getDefaultPosition();
        Pack.Info info = buildPackInfo(pack, description, compatibility);
        return Pack.create(pack.getId(), title, required, id -> pack.open(), info, PackType.CLIENT_RESOURCES, defaultPosition, fixedPosition, pack.getPackSource());
    }

    private static Pack.Info buildPackInfo(Pack pack, Component description, PackCompatibility compatibility) {
        int packVersion = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        if (compatibility == PackCompatibility.TOO_OLD) packVersion--;
        if (compatibility == PackCompatibility.TOO_NEW) packVersion++;
        return new Pack.Info(description, packVersion, pack.getRequestedFeatures());
    }
}
