package fuzs.resourcepackoverrides.core;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
