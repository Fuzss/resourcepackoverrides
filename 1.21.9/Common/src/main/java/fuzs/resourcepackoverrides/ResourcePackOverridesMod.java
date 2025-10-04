package fuzs.resourcepackoverrides;

import net.minecraft.resources.ResourceLocation;

public class ResourcePackOverridesMod extends ResourcePackOverrides {

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
