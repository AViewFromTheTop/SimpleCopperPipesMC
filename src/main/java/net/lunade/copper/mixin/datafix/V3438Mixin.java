package net.lunade.copper.mixin.datafix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import net.lunade.copper.SimpleCopperPipesSharedConstants;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3438;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(V3438.class)
public class V3438Mixin {

    @WrapOperation(
            method = "registerBlockEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/datafix/schemas/NamespacedSchema;registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;",
                    ordinal = 0
            )
    )
    public Map<String, Supplier<TypeTemplate>> wilderWild$registerBlockEntities(V3438 instance, Schema schema, Operation<Map<String, Supplier<TypeTemplate>>> original) {
        Map<String, Supplier<TypeTemplate>> map = original.call(instance, schema);
        schema.register(
                map,
			SimpleCopperPipesSharedConstants.legacyId("copper_pipe").toString(),
                () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
        );
        schema.register(
                map,
			SimpleCopperPipesSharedConstants.legacyId("copper_fitting").toString(),
                () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
        );

        schema.register(
                map,
                SimpleCopperPipesSharedConstants.id("copper_pipe").toString(),
                () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
        );
        schema.register(
                map,
                SimpleCopperPipesSharedConstants.id("copper_fitting").toString(),
                () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))
        );
        return map;
    }
}
