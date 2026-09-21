package net.lunade.copper.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import net.fabricmc.frozenblock.datafixer.api.DataFixerEntrypoint;
import net.fabricmc.frozenblock.datafixer.api.SchemaRegistry;
import net.lunade.copper.SCPConstants;
import net.minecraft.util.datafix.fixes.References;

public final class SCPDataFixerEntrypoint implements DataFixerEntrypoint {

	@Override
	public void onRegisterBlockEntities(SchemaRegistry registry, Schema schema) {
		registry.register(SCPConstants.legacyId("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.legacyId("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.id("copper_pipe"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
		registry.register(SCPConstants.id("copper_fitting"), () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
	}

	@Override
	public void onRegisterEntities(SchemaRegistry registry, Schema schema) {}
}
