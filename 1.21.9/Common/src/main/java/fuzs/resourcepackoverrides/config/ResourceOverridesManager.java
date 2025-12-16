package fuzs.resourcepackoverrides.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import fuzs.resourcepackoverrides.ResourcePackOverrides;
import fuzs.resourcepackoverrides.services.ClientAbstractions;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ResourceOverridesManager {
    private static final String FILE_NAME = ResourcePackOverrides.MOD_ID + "_v3.json";
    private static final Path FILE_PATH = ClientAbstractions.INSTANCE.getConfigDirectory().resolve(FILE_NAME);

    @Nullable
    private static PackConfig globalPackConfig;
    private static int failedReloads;

    public static PackOverrides getOverride(String id) {
        PackConfig packConfig = getGlobalPackConfig();
        return packConfig.getPackOverrides(id);
    }

    public static List<String> getDefaultResourcePacks(boolean failed) {
        PackConfig packConfig = getGlobalPackConfig();
        if (failed && ++failedReloads > packConfig.failedReloads().orElse(5)) {
            return ImmutableList.of();
        } else {
            return packConfig.defaultPacks();
        }
    }

    public static void clearGlobalPackConfig() {
        globalPackConfig = null;
    }

    private static PackConfig getGlobalPackConfig() {
        if (globalPackConfig == null) {
            return globalPackConfig = load();
        } else {
            return globalPackConfig;
        }
    }

    private static PackConfig load() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(FILE_PATH)) {
            JsonElement jsonElement = GsonHelper.parse(bufferedReader);
            return PackConfig.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(ResourcePackOverrides.LOGGER::error)
                    .orElse(PackConfig.DEFAULT);
        } catch (NoSuchFileException ignored) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(FILE_PATH); JsonWriter jsonWriter = new JsonWriter(
                    bufferedWriter)) {
                jsonWriter.setSerializeNulls(false);
                jsonWriter.setIndent("  ");
                Optional<JsonElement> jsonElement = PackConfig.CODEC.encodeStart(JsonOps.INSTANCE, PackConfig.DEFAULT)
                        .resultOrPartial(ResourcePackOverrides.LOGGER::error);
                if (jsonElement.isPresent()) {
                    GsonHelper.writeValue(jsonWriter, jsonElement.get(), null);
                }
            } catch (Exception exception) {
                ResourcePackOverrides.LOGGER.error("Unable to write {}", FILE_PATH, exception);
            }
        } catch (Exception exception) {
            ResourcePackOverrides.LOGGER.error("Unable to read {}", FILE_PATH, exception);
        }

        return PackConfig.DEFAULT;
    }
}
