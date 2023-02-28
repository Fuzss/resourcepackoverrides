package fuzs.resourcepackoverrides.core;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
