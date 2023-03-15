package fuzs.resourcepackoverrides.client.gui.screens.packs;

import net.minecraft.client.gui.screen.PackLoadingManager;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ForwardingPackSelectionModelEntry implements PackLoadingManager.IPack, PackAwareSelectionEntry {
    private final ResourcePackInfo pack;
    private final PackLoadingManager.IPack other;

    public ForwardingPackSelectionModelEntry(ResourcePackInfo pack, PackLoadingManager.IPack other) {
        this.pack = pack;
        this.other = other;
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
        return this.other.isFixedPosition();
    }

    @Override
    public boolean isRequired() {
        return this.other.isRequired();
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
