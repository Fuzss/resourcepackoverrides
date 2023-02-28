package fuzs.resourcepackoverrides.core;

import java.nio.file.Path;
import java.util.ServiceLoader;

public interface CommonAbstractions {

    CommonAbstractions INSTANCE = loadServiceProvider(CommonAbstractions.class);

    Path getConfigDirectory();

    private static <T> T loadServiceProvider(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
