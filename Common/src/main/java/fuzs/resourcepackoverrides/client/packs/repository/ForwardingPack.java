package fuzs.resourcepackoverrides.client.packs.repository;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import fuzs.resourcepackoverrides.client.data.ResourceOverridesManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;

public class ForwardingPack extends Pack {
    private final PackSelectionOverride override;

    private ForwardingPack(Pack other, PackSelectionOverride override) {
        super(other.getId(), other.isRequired(), other::open, other.getTitle(), other.getDescription(), other.getCompatibility(), other.getDefaultPosition(), other.isFixedPosition(), other.getPackSource());
        this.override = override;
    }

    public static Pack copy(Pack pack) {
        return new ForwardingPack(pack, ResourceOverridesManager.getOverride(pack.getId()));
    }

    @Override
    public Component getTitle() {
        return this.override.title() != null ? this.override.title() : super.getTitle();
    }

    @Override
    public Component getDescription() {
        return this.override.description() != null ? this.override.description() : super.getDescription();
    }

    @Override
    public PackCompatibility getCompatibility() {
        return this.override.compatibility() != null ? this.override.compatibility() : super.getCompatibility();
    }

    @Override
    public boolean isRequired() {
        return this.override.required() != null ? this.override.required() : Boolean.valueOf(super.isRequired());
    }

    @Override
    public boolean isFixedPosition() {
        return this.override.fixedPosition() != null ? this.override.fixedPosition() : Boolean.valueOf(super.isFixedPosition());
    }

    @Override
    public Position getDefaultPosition() {
        return this.override.defaultPosition() != null ? this.override.defaultPosition() : super.getDefaultPosition();
    }
}
