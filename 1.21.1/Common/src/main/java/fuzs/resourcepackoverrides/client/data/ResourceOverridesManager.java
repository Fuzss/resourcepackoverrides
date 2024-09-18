package fuzs.resourcepackoverrides.client.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.util.GsonHelper;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class ResourceOverridesManager {
    private static final String FILE_NAME = ResourcePackOverrides.MOD_ID + ".json";
    private static final String SCHEMA_VERSION = String.valueOf(2);
    private static final String GROUP_PREFIX = "$$";

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
        JsonConfigFileUtil.getAndLoad(FILE_NAME, (File file) -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("schema_version", SCHEMA_VERSION);
            JsonConfigFileUtil.saveToFile(file, jsonObject);
        }, ResourceOverridesManager::deserializeAllOverrides);
    }

    private static void deserializeAllOverrides(FileReader reader) {
        JsonElement jsonElement = JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
        JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "resource pack override");
        String schemaVersion = GsonHelper.getAsString(jsonObject, "schema_version", "1");
        if (!schemaVersion.equals(SCHEMA_VERSION)) {
            ResourcePackOverrides.LOGGER.warn("Outdated config schema! Config might not work correctly. Current schema is {}.", SCHEMA_VERSION);
        }
        failedReloads = GsonHelper.getAsInt(jsonObject, "failed_reloads_per_session", 5);
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
                JsonArray jsonArray = GsonHelper.convertToJsonArray(entry.getValue(), entry.getKey());
                List<String> groupIds = overrideGroups.computeIfAbsent(entry.getKey(), id -> Lists.newArrayList());
                for (JsonElement groupValue : jsonArray) {
                    groupIds.add(groupValue.getAsString());
                }
            }
        }
        ImmutableMap.Builder<String, PackSelectionOverride> builder = ImmutableMap.builder();
        String prefix = schemaVersion.equals("1") ? "$" : GROUP_PREFIX;
        for (Map.Entry<String, PackSelectionOverride> entry : packOverrides.entrySet()) {
            String id = entry.getKey();
            if (id.startsWith(prefix)) {
                List<String> groupIds = overrideGroups.get(id.substring(prefix.length()));
                if (groupIds == null) throw new IllegalArgumentException("Unknown group id %s".formatted(id));
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
        JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "resource pack override");
        Component title = getOptionalString(jsonObject, "title", s -> Component.Serializer.fromJson(s, RegistryAccess.EMPTY));
        Component description = getOptionalString(jsonObject, "description", s -> Component.Serializer.fromJson(s, RegistryAccess.EMPTY));
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
