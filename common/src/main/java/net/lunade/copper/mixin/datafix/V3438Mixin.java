package net.lunade.copper.mixin.datafix;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3438;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(V3438.class)
public class V3438Mixin {

	@Inject(method = "registerBlockEntities", at = @At("RETURN"))
	public void simpleCopperPipes$registerBlockEntities(
		Schema schema, CallbackInfoReturnable<Map<String, Supplier<TypeTemplate>>> info,
		@Local Map<String, Supplier<TypeTemplate>> map
	) {
		schema.register(
			map,
			SimpleCopperPipesConstants.legacyId("copper_pipe").toString(),
			() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
		);
		schema.register(
			map,
			SimpleCopperPipesConstants.legacyId("copper_fitting").toString(),
			() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
		);

		schema.register(
			map,
			SimpleCopperPipesConstants.id("copper_pipe").toString(),
			() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
		);
		schema.register(
			map,
			SimpleCopperPipesConstants.id("copper_fitting").toString(),
			() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
		);
	}
}
