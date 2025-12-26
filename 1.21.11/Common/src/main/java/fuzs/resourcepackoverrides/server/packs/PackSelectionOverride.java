package fuzs.resourcepackoverrides.server.packs;

import fuzs.resourcepackoverrides.config.ResourceOverridesManager;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record PackSelectionOverride(@Nullable Component title,
                                    @Nullable Component description,
                                    Pack.@Nullable Position defaultPosition,
                                    @Nullable PackCompatibility compatibility,
                                    @Nullable Boolean fixedPosition,
                                    @Nullable Boolean required,
                                    @Nullable Boolean hidden) {
    public static final PackSelectionOverride EMPTY = new PackSelectionOverride(null,
            null,
            null,
            null,
            null,
            null,
            null);

    public static void applyPackOverride(Pack pack) {
        ResourceOverridesManager.getOverride(pack.getId()).apply(pack);
    }

    public void apply(Pack pack) {
        if (this.title != null && !Objects.equals(this.title, pack.getTitle())) {
            pack.location = new PackLocationInfo(pack.location.id(),
                    this.title,
                    pack.location.source(),
                    pack.location.knownPackInfo());
        }

        if (this.required != null && !Objects.equals(this.required, pack.isRequired())
                || this.defaultPosition != null && !Objects.equals(this.defaultPosition, pack.getDefaultPosition())
                || this.fixedPosition != null && !Objects.equals(this.fixedPosition, pack.isFixedPosition())) {
            boolean required = this.required != null ? this.required : pack.isRequired();
            Pack.Position defaultPosition =
                    this.defaultPosition != null ? this.defaultPosition : pack.getDefaultPosition();
            boolean fixedPosition = this.fixedPosition != null ? this.fixedPosition : pack.isFixedPosition();
            pack.selectionConfig = new PackSelectionConfig(required, defaultPosition, fixedPosition);
        }

        if (this.description != null && !Objects.equals(this.description, pack.getDescription())
                || this.compatibility != null && !Objects.equals(this.compatibility, pack.getCompatibility())
                || this.hidden != null && !Objects.equals(this.hidden,
                ClientAbstractions.INSTANCE.isPackHidden(pack))) {
            Component description = this.description != null ? this.description : pack.getDescription();
            PackCompatibility compatibility =
                    this.compatibility != null ? this.compatibility() : pack.getCompatibility();
            boolean hidden = this.hidden != null ? this.hidden : ClientAbstractions.INSTANCE.isPackHidden(pack);
            pack.metadata = ClientAbstractions.INSTANCE.createPackInfo(description,
                    compatibility,
                    pack.getRequestedFeatures(),
                    pack.metadata.overlays(),
                    hidden);
            ClientAbstractions.INSTANCE.setPackHidden(pack, hidden);
        }
    }
}
