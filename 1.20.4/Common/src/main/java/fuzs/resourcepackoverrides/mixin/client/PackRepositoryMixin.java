package fuzs.resourcepackoverrides.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.resourcepackoverrides.client.util.ForwardingPackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;

@Mixin(PackRepository.class)
abstract class PackRepositoryMixin {

    @ModifyReturnValue(method = "discoverAvailable", at = @At("TAIL"))
    private Map<String, Pack> discoverAvailable(Map<String, Pack> packs) {
        // Wrap only on resource pack selection screen, we don't want to mess with data packs.
        if (PackRepository.class.cast(this) != Minecraft.getInstance().getResourcePackRepository()) return packs;
        return packs.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ForwardingPackHelper.copyAndOverride(entry.getValue())));
    }

    @ModifyReturnValue(method = "rebuildSelected", at = @At("TAIL"))
    private List<Pack> rebuildSelected(List<Pack> packs) {
        // Wrap only on resource pack selection screen, we don't want to mess with data packs.
        if (PackRepository.class.cast(this) != Minecraft.getInstance().getResourcePackRepository()) return packs;
        int i = 0;
        for (; i < packs.size(); i++) {
            // the user has moved folder resource packs below vanilla for some reason, don't change anything
            if (packs.get(i).getDefaultPosition() != Pack.Position.BOTTOM) return packs;
            if (packs.get(i).getId().equals("vanilla")) break;
        }
        if (i != 0) {
            // there are some bottom packs below vanilla, this is pointless, move vanilla to the bottom
            // we do this mainly so a server provided pack can be moved to the bottom above vanilla automatically,
            // so user resource packs still apply above the server pack
            packs = Lists.newArrayList(packs);
            packs.add(0, packs.remove(i));
        }
        return packs;
    }
}
