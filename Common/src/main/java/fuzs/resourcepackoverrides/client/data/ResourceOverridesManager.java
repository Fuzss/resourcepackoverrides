package fuzs.resourcepackoverrides.client.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class ResourceOverridesManager {
    private static final String FILE_NAME = ResourcePackOverrides.MOD_ID + ".json";
    private static final String SCHEMA_VERSION = String.valueOf(1);
    private static final Map<String, PackSelectionOverride> OVERRIDES_BY_ID = Maps.newHashMap();
    private static List<String> defaultResourcePacks;
    private static PackSelectionOverride defaultOverride;
    private static int failedReloads;
    public static boolean debugTooltips;

    public static PackSelectionOverride getOverride(String id) {
        if (defaultOverride == null) load();
        return OVERRIDES_BY_ID.getOrDefault(id, defaultOverride);
    }

    public static List<String> getDefaultResourcePacks(boolean failed) {
        if (defaultResourcePacks == null) load();
        if (failed && --failedReloads < 0) return ImmutableList.of();
        return defaultResourcePacks;
    }

    private static void load() {
        defaultResourcePacks = ImmutableList.of();
        defaultOverride = PackSelectionOverride.EMPTY;
        JsonConfigFileUtil.getAndLoad(FILE_NAME, file -> {}, ResourceOverridesManager::deserializeAllOverrides);
    }

    private static void deserializeAllOverrides(FileReader reader) {
        OVERRIDES_BY_ID.clear();
        JsonElement jsonElement = JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
        JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "resource pack override");
        if (!GsonHelper.getAsString(jsonObject, "schema_version").equals(SCHEMA_VERSION)) throw new IllegalArgumentException("wrong config schema present");
        failedReloads = GsonHelper.getAsInt(jsonObject, "failed_reloads_per_session", 5);
        if (jsonObject.has("default_packs")) {
            JsonArray resourcePacks = jsonObject.getAsJsonArray("default_packs");
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (JsonElement resourcePack : resourcePacks) {
                builder.add(resourcePack.getAsString());
            }
            defaultResourcePacks = builder.build();
        }
        debugTooltips = GsonHelper.getAsBoolean(jsonObject, "debug_tooltips", false);
        if (jsonObject.has("default_overrides")) {
            defaultOverride = deserializeOverrideEntry(jsonObject.get("default_overrides"));
        }
        if (!jsonObject.has("pack_overrides")) return;
        JsonObject overrides = jsonObject.getAsJsonObject("pack_overrides");
        for (Map.Entry<String, JsonElement> entry : overrides.entrySet()) {
            OVERRIDES_BY_ID.put(entry.getKey(), deserializeOverrideEntry(entry.getValue()));
        }
    }

    private static PackSelectionOverride deserializeOverrideEntry(JsonElement jsonElement) {
        JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "resource pack override");
        Component title = getOptionalString(jsonObject, "title", Component.Serializer::fromJson);
        Component description = getOptionalString(jsonObject, "description", Component.Serializer::fromJson);
        Pack.Position defaultPosition = getOptionalString(jsonObject, "default_position", s -> {
            try {
                return Pack.Position.valueOf(s.toUpperCase(Locale.ROOT));
            } catch (Exception ignored) {
                return null;
            }
        });
        PackCompatibility compatible = GsonHelper.getAsBoolean(jsonObject, "force_compatible", false) ? PackCompatibility.COMPATIBLE : null;
        Boolean fixedPosition = getOptionalFlag(jsonObject, "fixed_position");
        Boolean required = GsonHelper.getAsBoolean(jsonObject, "required", false) ? true : null;
        Boolean hidden = GsonHelper.getAsBoolean(jsonObject, "hidden", false) ? true : null;
        return new PackSelectionOverride(title, description, defaultPosition, compatible, fixedPosition, required, hidden);
    }

    @Nullable
    private static <T> T getOptionalString(JsonObject jsonObject, String memberName, Function<String, T> converter) {
        return jsonObject.has(memberName) ? converter.apply(GsonHelper.getAsString(jsonObject, memberName)) : null;
    }

    @Nullable
    private static Boolean getOptionalFlag(JsonObject jsonObject, String memberName) {
        return jsonObject.has(memberName) ? jsonObject.get(memberName).getAsBoolean() : null;
    }
}
