package fuzs.resourcepackoverrides;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcePackOverrides {
    public static final String MOD_ID = "resourcepackoverrides";
    public static final String MOD_NAME = "Resource Pack Overrides";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
