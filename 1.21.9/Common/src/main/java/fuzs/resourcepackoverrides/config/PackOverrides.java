package fuzs.resourcepackoverrides.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import fuzs.resourcepackoverrides.util.CodecExtras;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;

import java.util.Locale;
import java.util.Optional;

public record PackOverrides(Optional<Component> title,
                            Optional<Component> description,
                            Optional<Pack.Position> defaultPosition,
                            Optional<PackCompatibility> compatibility,
                            Optional<Boolean> fixedPosition,
                            Optional<Boolean> required,
                            Optional<Boolean> hidden) {
    public static final PackOverrides DEFAULT = new PackOverrides(Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(PackCompatibility.COMPATIBLE),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
    public static final Codec<PackOverrides> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(PackOverrides::title),
            ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(PackOverrides::description),
            CodecExtras.fromEnumWithMapping(Pack.Position.class::getEnumConstants, (Pack.Position position) -> {
                return position.name().toUpperCase(Locale.ROOT);
            }).optionalFieldOf("default_position").forGetter(PackOverrides::defaultPosition),
            Codec.BOOL.flatXmap((Boolean compatible) -> {
                                return compatible ? DataResult.success(PackCompatibility.COMPATIBLE) :
                                        DataResult.error(() -> "Only 'true' is allowed!");
                            },
                            (PackCompatibility compatibility) -> compatibility.isCompatible() ?
                                    DataResult.success(Boolean.TRUE) : DataResult.error(() -> "Only 'COMPATIBLE' is allowed!"))
                    .optionalFieldOf("force_compatible")
                    .forGetter(PackOverrides::compatibility),
            Codec.BOOL.optionalFieldOf("fixed_position").forGetter(PackOverrides::fixedPosition),
            Codec.BOOL.optionalFieldOf("required").forGetter(PackOverrides::required),
            Codec.BOOL.optionalFieldOf("hidden").forGetter(PackOverrides::hidden)).apply(instance, PackOverrides::new));

    public static void applyPackOverride(Pack pack) {
        ResourceOverridesManager.getOverride(pack.getId()).apply(pack);
    }

    public boolean notHidden() {
        return this.hidden().isEmpty() || !this.hidden().get();
    }

    public void apply(Pack pack) {
        pack.location = this.title.map((Component component) -> {
            return new PackLocationInfo(pack.location.id(),
                    component,
                    pack.location.source(),
                    pack.location.knownPackInfo());
        }).orElse(pack.location);

        if (this.required.isPresent() || this.fixedPosition.isPresent() || this.defaultPosition.isPresent()) {
            pack.selectionConfig = new PackSelectionConfig(this.required.orElseGet(pack.selectionConfig::required),
                    this.defaultPosition.orElseGet(pack.selectionConfig::defaultPosition),
                    this.fixedPosition.orElseGet(pack.selectionConfig::fixedPosition));
        }

        if (this.description.isPresent() || this.compatibility.isPresent() || this.hidden.isPresent()) {
            pack.metadata = ClientAbstractions.INSTANCE.createPackInfo(this.description.orElseGet(pack::getDescription),
                    this.compatibility.orElseGet(pack::getCompatibility),
                    pack.getRequestedFeatures(),
                    pack.metadata.overlays(),
                    this.hidden.orElseGet(() -> {
                        return ClientAbstractions.INSTANCE.isPackHidden(pack);
                    }));
        }
    }
}
