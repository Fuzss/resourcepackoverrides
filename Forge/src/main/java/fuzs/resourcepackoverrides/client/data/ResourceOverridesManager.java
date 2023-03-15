package fuzs.resourcepackoverrides.client.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.resources.PackCompatibility;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.io.FileReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class ResourceOverridesManager {
    private static final String FILE_NAME = ResourcePackOverrides.MOD_ID + ".json";
    private static final String SCHEMA_VERSION = String.valueOf(1);
    private static Map<String, PackSelectionOverride> overridesById = Maps.newHashMap();
    private static List<String> defaultResourcePacks;
    private static PackSelectionOverride defaultOverride;
    private static int failedReloads;

    public static PackSelectionOverride getOverride(String id) {
        if (defaultOverride == null) load();
        return overridesById.getOrDefault(id, defaultOverride);
    }

    public static List<String> getDefaultResourcePacks(boolean failed) {
        if (defaultResourcePacks == null) load();
        if (failed && --failedReloads < 0) return ImmutableList.of();
        return defaultResourcePacks;
    }

    public static void load() {
        defaultResourcePacks = ImmutableList.of();
        defaultOverride = PackSelectionOverride.EMPTY;
        JsonConfigFileUtil.getAndLoad(FILE_NAME, file -> {}, ResourceOverridesManager::deserializeAllOverrides);
    }

    private static void deserializeAllOverrides(FileReader reader) {
        JsonElement jsonElement = JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
        JsonObject jsonObject = JSONUtils.convertToJsonObject(jsonElement, "resource pack override");
        if (!JSONUtils.getAsString(jsonObject, "schema_version").equals(SCHEMA_VERSION)) throw new IllegalArgumentException("wrong config schema present");
        failedReloads = JSONUtils.getAsInt(jsonObject, "failed_reloads_per_session", 5);
        if (jsonObject.has("default_packs")) {
            JsonArray resourcePacks = jsonObject.getAsJsonArray("default_packs");
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (JsonElement resourcePack : resourcePacks) {
                builder.add(resourcePack.getAsString());
            }
            defaultResourcePacks = builder.build();
        }
        if (jsonObject.has("default_overrides")) {
            defaultOverride = deserializeOverrideEntry(jsonObject.get("default_overrides"));
        }
        if (!jsonObject.has("pack_overrides")) return;
        Map<String, PackSelectionOverride> packOverrides = Maps.newHashMap();
        Map<String, List<String>> overrideGroups = Maps.newHashMap();
        JsonObject overrides = jsonObject.getAsJsonObject("pack_overrides");
        for (Map.Entry<String, JsonElement> entry : overrides.entrySet()) {
            JsonElement packOverride = entry.getValue();
            if (packOverride.isJsonObject()) {
                packOverrides.put(entry.getKey(), deserializeOverrideEntry(packOverride));
            } else if (packOverride.isJsonArray()) {
                JsonArray jsonArray = JSONUtils.convertToJsonArray(entry.getValue(), entry.getKey());
                List<String> groupIds = overrideGroups.computeIfAbsent(entry.getKey(), id -> Lists.newArrayList());
                for (JsonElement groupValue : jsonArray) {
                    groupIds.add(groupValue.getAsString());
                }
            }
        }
        ImmutableMap.Builder<String, PackSelectionOverride> builder = ImmutableMap.builder();
        for (Map.Entry<String, PackSelectionOverride> entry : packOverrides.entrySet()) {
            String id = entry.getKey();
            if (id.startsWith("$")) {
                List<String> groupIds = overrideGroups.get(id.substring(1));
                if (groupIds == null) throw new IllegalArgumentException(String.format("Unknown group id %s", id));
                for (String groupId : groupIds) {
                    builder.put(groupId, entry.getValue());
                }
            } else {
                builder.put(id, entry.getValue());
            }
        }
        overridesById = builder.build();
    }

    private static PackSelectionOverride deserializeOverrideEntry(JsonElement jsonElement) {
        JsonObject jsonObject = JSONUtils.convertToJsonObject(jsonElement, "resource pack override");
        ITextComponent title = getOptionalString(jsonObject, "title", ITextComponent.Serializer::fromJson);
        ITextComponent description = getOptionalString(jsonObject, "description", ITextComponent.Serializer::fromJson);
        ResourcePackInfo.Priority defaultPosition = getOptionalString(jsonObject, "default_position", s -> {
            try {
                return ResourcePackInfo.Priority.valueOf(s.toUpperCase(Locale.ROOT));
            } catch (Exception ignored) {
                return null;
            }
        });
        PackCompatibility compatible = JSONUtils.getAsBoolean(jsonObject, "force_compatible", false) ? PackCompatibility.COMPATIBLE : null;
        Boolean fixedPosition = getOptionalFlag(jsonObject, "fixed_position");
        Boolean required = JSONUtils.getAsBoolean(jsonObject, "required", false) ? true : null;
        Boolean hidden = JSONUtils.getAsBoolean(jsonObject, "hidden", false) ? true : null;
        return new PackSelectionOverride(title, description, defaultPosition, compatible, fixedPosition, required, hidden);
    }

    @Nullable
    private static <T> T getOptionalString(JsonObject jsonObject, String memberName, Function<String, T> converter) {
        return jsonObject.has(memberName) ? converter.apply(JSONUtils.getAsString(jsonObject, memberName)) : null;
    }

    @Nullable
    private static Boolean getOptionalFlag(JsonObject jsonObject, String memberName) {
        return jsonObject.has(memberName) ? jsonObject.get(memberName).getAsBoolean() : null;
    }
}
