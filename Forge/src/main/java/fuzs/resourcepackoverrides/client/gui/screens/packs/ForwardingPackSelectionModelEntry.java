package fuzs.resourcepackoverrides.client.gui.screens.packs;

import fuzs.resourcepackoverrides.client.data.PackSelectionOverride;
import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ForwardingPackSelectionModelEntry implements PackLoadingManager.IPack, PackAwareSelectionEntry {
    private final ResourcePackInfo pack;
    private final PackLoadingManager.IPack other;
    private final PackSelectionOverride override;

    public ForwardingPackSelectionModelEntry(ResourcePackInfo pack, PackLoadingManager.IPack other, PackSelectionOverride override) {
        this.pack = pack;
        this.other = other;
        this.override = override;
    }

    @Override
    public String getPackId() {
        return this.pack.getId();
    }

    @Override
    public ResourceLocation getIconTexture() {
        return this.other.getIconTexture();
    }

    @Override
    public PackCompatibility getCompatibility() {
        if (this.override.forceCompatible()) {
            return PackCompatibility.COMPATIBLE;
        }
        return this.other.getCompatibility();
    }

    @Override
    public ITextComponent getTitle() {
        return this.other.getTitle();
    }

    @Override
    public ITextComponent getDescription() {
        return this.other.getDescription();
    }

    @Override
    public IPackNameDecorator getPackSource() {
        return this.other.getPackSource();
    }

    @Override
    public boolean isFixedPosition() {
        return this.override.fixedPosition() || this.other.isFixedPosition();
    }

    @Override
    public boolean isRequired() {
        return this.override.required() || this.other.isRequired();
    }

    @Override
    public void select() {
        this.other.select();
    }

    @Override
    public void unselect() {
        this.other.unselect();
    }

    @Override
    public void moveUp() {
        this.other.moveUp();
    }

    @Override
    public void moveDown() {
        this.other.moveDown();
    }

    @Override
    public boolean isSelected() {
        return this.other.isSelected();
    }

    @Override
    public boolean canMoveUp() {
        return this.other.canMoveUp();
    }

    @Override
    public boolean canMoveDown() {
        return this.other.canMoveDown();
    }
}
