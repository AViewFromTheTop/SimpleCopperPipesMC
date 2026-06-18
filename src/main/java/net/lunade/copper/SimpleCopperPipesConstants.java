package net.lunade.copper;

import net.minecraft.resources.Identifier;

public class SimpleCopperPipesConstants {
	public static final int CURRENT_FIX_VERSION = 6;
	public static final String MOD_ID = "simple_copper_pipes";
	public static final String LEGACY_NAMESPACE = "lunade";
	public static final String NAMESPACE = MOD_ID;

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(NAMESPACE, path);
	}

	public static Identifier legacyId(String path) {
		return Identifier.fromNamespaceAndPath(LEGACY_NAMESPACE, path);
	}

	public static Identifier legacyColoredPipe(String colour) {
		return legacyId(colour + "_pipe");
	}

	public static Identifier legacyGlowingPipe(String colour) {
		return legacyId("glowing_" + colour + "_pipe");
	}

	public static Identifier legacyColoredFitting(String colour) {
		return legacyId(colour + "_fitting");
	}

	public static Identifier legacyGlowingFitting(String colour) {
		return legacyId("glowing_" + colour + "_fitting");
	}
}
