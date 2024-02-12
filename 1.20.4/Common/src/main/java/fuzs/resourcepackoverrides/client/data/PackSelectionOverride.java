package fuzs.resourcepackoverrides.client.data;

import fuzs.resourcepackoverrides.ClientAbstractions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.jetbrains.annotations.Nullable;

public record PackSelectionOverride(@Nullable Component title,
                                    @Nullable Component description,
                                    @Nullable Pack.Position defaultPosition,
                                    @Nullable PackCompatibility compatibility,
                                    @Nullable Boolean fixedPosition,
                                    @Nullable Boolean required,
                                    @Nullable Boolean hidden) {

    public static final PackSelectionOverride EMPTY = new PackSelectionOverride(null, null, null, null, null, null,
            null
    );

    public static void applyPackOverride(Pack pack) {
        ResourceOverridesManager.getOverride(pack.getId()).apply(pack);
    }

    public void apply(Pack pack) {
        if (this.title() != null) {
            pack.title = this.title();
        }
        if (this.required() != null) {
            pack.required = this.required();
        }
        if (this.fixedPosition() != null) {
            pack.fixedPosition = this.fixedPosition();
        }
        if (this.defaultPosition() != null) {
            pack.defaultPosition = this.defaultPosition();
        }
        if (this.description() != null || this.compatibility() != null || this.hidden() != null) {
            pack.info = createPackInfoOverride(pack, this);
        }
    }

    private static Pack.Info createPackInfoOverride(Pack pack, PackSelectionOverride override) {
        Component description = override.description() != null ? override.description() : pack.getDescription();
        PackCompatibility compatibility =
                override.compatibility() != null ? override.compatibility() : pack.getCompatibility();
        boolean hidden = override.hidden() != null ? override.hidden() : ClientAbstractions.isPackHidden(pack);
        return ClientAbstractions.createPackInfo(description, compatibility, pack.getRequestedFeatures(),
                pack.info.overlays(), hidden
        );
    }
}
