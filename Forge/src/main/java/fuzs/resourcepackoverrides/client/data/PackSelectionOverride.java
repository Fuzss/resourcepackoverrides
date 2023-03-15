package fuzs.resourcepackoverrides.client.data;

import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Objects;

public final class PackSelectionOverride {
    public static final PackSelectionOverride EMPTY = new PackSelectionOverride(null, null, null, null, null, null, null);

    private final @Nullable ITextComponent title;
    private final @Nullable ITextComponent description;
    private final @Nullable ResourcePackInfo.Priority defaultPosition;
    private final @Nullable PackCompatibility compatibility;
    private final @Nullable Boolean fixedPosition;
    private final @Nullable Boolean required;
    private final @Nullable Boolean hidden;

    public PackSelectionOverride(@Nullable ITextComponent title, @Nullable ITextComponent description,
                                 @Nullable ResourcePackInfo.Priority defaultPosition, @Nullable PackCompatibility compatibility,
                                 @Nullable Boolean fixedPosition, @Nullable Boolean required,
                                 @Nullable Boolean hidden) {
        this.title = title;
        this.description = description;
        this.defaultPosition = defaultPosition;
        this.compatibility = compatibility;
        this.fixedPosition = fixedPosition;
        this.required = required;
        this.hidden = hidden;
    }

    public @Nullable ITextComponent title() {
        return title;
    }

    public @Nullable ITextComponent description() {
        return description;
    }

    public @Nullable ResourcePackInfo.Priority defaultPosition() {
        return defaultPosition;
    }

    public @Nullable PackCompatibility compatibility() {
        return compatibility;
    }

    public @Nullable Boolean fixedPosition() {
        return fixedPosition;
    }

    public @Nullable Boolean required() {
        return required;
    }

    public @Nullable Boolean hidden() {
        return hidden;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, defaultPosition, compatibility, fixedPosition, required, hidden);
    }

    @Override
    public String toString() {
        return "PackSelectionOverride[" +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "defaultPosition=" + defaultPosition + ", " +
                "compatibility=" + compatibility + ", " +
                "fixedPosition=" + fixedPosition + ", " +
                "required=" + required + ", " +
                "hidden=" + hidden + ']';
    }
}
