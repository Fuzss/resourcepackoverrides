package fuzs.resourcepackoverrides.client.util;

import fuzs.resourcepackoverrides.client.core.ClientAbstractions;
import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;

public class ForwardingPackHelper {

    public static Pack copyAndOverride(Pack pack) {
        PackSelectionOverride override = ResourceOverridesManager.getOverride(pack.getId());
        Component title = override.title() != null ? override.title() : pack.getTitle();
        Component description = override.description() != null ? override.description() : pack.getDescription();
        PackCompatibility compatibility = override.compatibility() != null ? override.compatibility() : pack.getCompatibility();
        boolean required = override.required() != null ? override.required() : pack.isRequired();
        boolean fixedPosition = override.fixedPosition() != null ? override.fixedPosition() : pack.isFixedPosition();
        Pack.Position defaultPosition = override.defaultPosition() != null ? override.defaultPosition() : pack.getDefaultPosition();
        boolean hidden = override.hidden() != null ? override.hidden() : ClientAbstractions.INSTANCE.isPackHidden(pack);
        Pack.Info info = rebuildPackInfo(pack, description, compatibility, hidden);
        return Pack.create(pack.getId(), title, required, id -> pack.open(), info, PackType.CLIENT_RESOURCES, defaultPosition, fixedPosition, pack.getPackSource());
    }

    private static Pack.Info rebuildPackInfo(Pack pack, Component description, PackCompatibility compatibility, boolean hidden) {
        int packVersion = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        if (compatibility == PackCompatibility.TOO_OLD) packVersion--;
        if (compatibility == PackCompatibility.TOO_NEW) packVersion++;
        return ClientAbstractions.INSTANCE.createPackInfo(description, packVersion, pack.getRequestedFeatures(), hidden);
    }
}
