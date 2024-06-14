package net.lunade.copper;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SimpleCopperPipesSharedConstants {
    public static final int CURRENT_FIX_VERSION = 4;
    public static final String MOD_ID = "simple_copper_pipes";
    public static final String LEGACY_NAMESPACE = "lunade";
    public static final String NAMESPACE = MOD_ID;

    @Contract("_ -> new")
    public static @NotNull ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(NAMESPACE, path);
    }

	@Contract("_ -> new")
	public static @NotNull ResourceLocation legacyId(String path) {
		return ResourceLocation.tryBuild(LEGACY_NAMESPACE, path);
	}
}
