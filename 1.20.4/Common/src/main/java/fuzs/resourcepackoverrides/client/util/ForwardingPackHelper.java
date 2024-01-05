package fuzs.resourcepackoverrides.client.util;

import fuzs.resourcepackoverrides.ClientAbstractions;
import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.network.chat.Component;
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
        boolean hidden = override.hidden() != null ? override.hidden() : ClientAbstractions.isPackHidden(pack);
        Pack.Info info = ClientAbstractions.createPackInfo(description, compatibility, pack.getRequestedFeatures(), pack.info.overlays(), hidden);
        Pack newPack = Pack.create(pack.getId(), title, required, pack.resources, info, defaultPosition, fixedPosition, pack.getPackSource());
        return ClientAbstractions.copyChildren(pack, newPack);
    }
}
