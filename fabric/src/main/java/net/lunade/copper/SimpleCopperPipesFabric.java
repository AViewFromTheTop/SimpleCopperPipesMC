package net.lunade.copper;

import net.fabricmc.loader.api.ModContainer;
import net.frozenblock.lib.entrypoint.api.FrozenModInitializer;

public final class SimpleCopperPipesFabric extends FrozenModInitializer {

	public SimpleCopperPipesFabric() {
		super(SCPConstants.MOD_ID);
	}

	@Override
	public void onInitialize(String modId, ModContainer container) {
		SimpleCopperPipes.init();
		SimpleCopperPipes.setup();
	}
}
