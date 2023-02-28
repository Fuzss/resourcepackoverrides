package fuzs.resourcepackoverrides.client.data;

import java.util.Objects;

public final class PackSelectionOverride {
    private final boolean forceCompatible;
    private final boolean fixedPosition;
    private final boolean required;
    private final boolean hidden;

    PackSelectionOverride(boolean forceCompatible, boolean fixedPosition, boolean required, boolean hidden) {
        this.forceCompatible = forceCompatible;
        this.fixedPosition = fixedPosition;
        this.required = required;
        this.hidden = hidden;
    }

    public boolean forceCompatible() {
        return forceCompatible;
    }

    public boolean fixedPosition() {
        return fixedPosition;
    }

    public boolean required() {
        return required;
    }

    public boolean hidden() {
        return hidden;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        PackSelectionOverride that = (PackSelectionOverride) obj;
        return this.forceCompatible == that.forceCompatible &&
                this.fixedPosition == that.fixedPosition &&
                this.required == that.required &&
                this.hidden == that.hidden;
    }

    @Override
    public int hashCode() {
        return Objects.hash(forceCompatible, fixedPosition, required, hidden);
    }

    @Override
    public String toString() {
        return "PackSelectionOverride[" +
                "forceCompatible=" + forceCompatible + ", " +
                "fixedPosition=" + fixedPosition + ", " +
                "required=" + required + ", " +
                "hidden=" + hidden + ']';
    }

    public static final PackSelectionOverride EMPTY = new PackSelectionOverride(false, false, false, false);
}
