package fuzs.resourcepackoverrides.client.gui.screens.packs;

import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;

public class ForwardingPackSelectionModelEntry implements PackSelectionModel.Entry, PackAwareSelectionEntry {
    private final Pack pack;
    private final PackSelectionModel.Entry other;

    public ForwardingPackSelectionModelEntry(Pack pack, PackSelectionModel.Entry other) {
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
    public String getId() {
        return this.other.getId();
    }

    @Override
    public Component getTitle() {
        return this.other.getTitle();
    }

    @Override
    public Component getDescription() {
        return this.other.getDescription();
    }

    @Override
    public PackSource getPackSource() {
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
