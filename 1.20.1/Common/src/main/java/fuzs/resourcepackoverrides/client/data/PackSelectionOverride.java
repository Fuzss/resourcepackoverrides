package fuzs.resourcepackoverrides.client.data;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.jetbrains.annotations.Nullable;

public record PackSelectionOverride(@Nullable Component title, @Nullable Component description,
                                    @Nullable Pack.Position defaultPosition, @Nullable PackCompatibility compatibility,
                                    @Nullable Boolean fixedPosition, @Nullable Boolean required,
                                    @Nullable Boolean hidden) {

    public static final PackSelectionOverride EMPTY = new PackSelectionOverride(null, null, null, null, null, null, null);
}
