package fuzs.resourcepackoverrides.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.resourcepackoverrides.util.CodecExtras;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record PackConfig(Optional<Integer> failedReloads,
                         Optional<PackOverrides> defaultOverrides,
                         List<String> defaultPacks,
                         Map<String, PackOverrides> packOverrides) {
    public static final PackConfig DEFAULT = new PackConfig(Optional.of(5),
            Optional.of(PackOverrides.DEFAULT),
            List.of(),
            Map.of());
    public static final Codec<PackConfig> CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<PackConfig> instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("failed_reloads_per_session")
                    .forGetter(PackConfig::failedReloads),
            PackOverrides.CODEC.optionalFieldOf("default_overrides").forGetter(PackConfig::defaultOverrides),
            Codec.STRING.listOf().optionalFieldOf("fixed_position", List.of()).forGetter(PackConfig::defaultPacks),
            CodecExtras.mapOf(ExtraCodecs.compactListCodec(Codec.STRING).fieldOf("packs"),
                            PackOverrides.CODEC.fieldOf("overrides"))
                    .xmap((Map<List<String>, PackOverrides> map) -> {
                                return map.entrySet()
                                        .stream()
                                        .mapMulti((Map.Entry<List<String>, PackOverrides> entry, Consumer<Map.Entry<String, PackOverrides>> consumer) -> entry.getKey()
                                                .forEach((String key) -> consumer.accept(Map.entry(key, entry.getValue()))))
                                        .collect(Collectors.toMap(Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (PackOverrides o1, PackOverrides o2) -> o1));
                            },
                            (Map<String, PackOverrides> map) -> map.entrySet()
                                    .stream()
                                    .collect(Collectors.toMap((Map.Entry<String, PackOverrides> entry) -> List.of(entry.getKey()),
                                            Map.Entry::getValue)))
                    .optionalFieldOf("pack_overrides", Map.of())
                    .forGetter(PackConfig::packOverrides)).apply(instance, PackConfig::new));

    public PackOverrides getPackOverrides(String pack) {
        return this.packOverrides.getOrDefault(pack, this.defaultOverrides.orElse(PackOverrides.DEFAULT));
    }
}
