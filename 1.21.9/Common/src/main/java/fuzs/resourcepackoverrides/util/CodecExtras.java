package fuzs.resourcepackoverrides.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Additional codecs similar to {@link ExtraCodecs}.
 */
public final class CodecExtras {

    private CodecExtras() {
        // NO-OP
    }

    /**
     * A map codec that allows for key values that are not encoded as {@link net.minecraft.nbt.StringTag}, unlike
     * {@link Codec#unboundedMap(Codec, Codec)}.
     *
     * @param keyCodec   the map key codec
     * @param valueCodec the map value codec
     * @param <K>        the map key type
     * @param <V>        the map value type
     * @return the codec
     */
    public static <K, V> Codec<Map<K, V>> mapOf(Codec<K> keyCodec, Codec<V> valueCodec) {
        return mapOf(keyCodec.fieldOf("key"), valueCodec.fieldOf("value"));
    }

    /**
     * A map codec that allows for key values that are not encoded as {@link net.minecraft.nbt.StringTag}, unlike
     * {@link Codec#unboundedMap(Codec, Codec)}
     *
     * @param keyCodec   the map key codec
     * @param valueCodec the map value codec
     * @param <K>        the map key type
     * @param <V>        the map value type
     * @return the codec
     */
    public static <K, V> Codec<Map<K, V>> mapOf(MapCodec<K> keyCodec, MapCodec<V> valueCodec) {
        return Codec.mapPair(keyCodec, valueCodec).codec().listOf().xmap((List<Pair<K, V>> list) -> {
                    return list.stream()
                            .collect(ImmutableMap.<Pair<K, V>, K, V>toImmutableMap(Pair::getFirst, Pair::getSecond));
                },
                (Map<K, V> map) -> map.entrySet()
                        .stream()
                        .map((Map.Entry<K, V> entry) -> new Pair<>(entry.getKey(), entry.getValue()))
                        .toList());
    }

    /**
     * Create an {@link Enum} codec.
     *
     * @param enumClazz the enum class
     * @param <E>       the enum type
     * @return the codec
     */
    public static <E extends Enum<E>> Codec<E> fromEnum(Class<E> enumClazz) {
        return fromEnum(enumClazz::getEnumConstants);
    }

    /**
     * Create an {@link Enum} codec.
     *
     * @param enumValues the enum values
     * @param <E>        the enum type
     * @return the codec
     */
    public static <E extends Enum<E>> Codec<E> fromEnum(Supplier<E[]> enumValues) {
        return fromEnumWithMapping(enumValues, (E enumConstant) -> enumConstant.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Create an {@link Enum} codec.
     *
     * @param enumValues  the enum values
     * @param keyFunction the string key extractor
     * @param <E>         the enum type
     * @return the codec
     */
    public static <E extends Enum<E>> Codec<E> fromEnumWithMapping(Supplier<E[]> enumValues, Function<E, String> keyFunction) {
        E[] enums = enumValues.get();
        Function<String, E> function = Arrays.stream(enums)
                .collect(ImmutableMap.toImmutableMap(keyFunction, Function.identity()))::get;
        return Codec.stringResolver(keyFunction, function);
    }
}
