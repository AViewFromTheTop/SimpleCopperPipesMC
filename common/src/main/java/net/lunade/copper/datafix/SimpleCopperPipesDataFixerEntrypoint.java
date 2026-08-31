package net.lunade.copper.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.frozenblock.datafixer.api.DataFixerEntrypoint;
import net.fabricmc.frozenblock.datafixer.api.SchemaRegistry;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.util.datafix.fixes.References;

public final class SimpleCopperPipesDataFixerEntrypoint implements DataFixerEntrypoint {

	@Override
	public void onRegisterBlockEntities(SchemaRegistry registry, Schema schema) {
		registry.register(SimpleCopperPipesConstants.legacyId("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SimpleCopperPipesConstants.legacyId("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SimpleCopperPipesConstants.id("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SimpleCopperPipesConstants.id("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
	}

	@Override
	public void onRegisterEntities(SchemaRegistry registry, Schema schema) {
	}
}
