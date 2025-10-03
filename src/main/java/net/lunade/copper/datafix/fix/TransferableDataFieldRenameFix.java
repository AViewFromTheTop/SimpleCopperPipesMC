package net.lunade.copper.datafix.fix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.lunade.copper.SimpleCopperPipesConstants;
import net.minecraft.util.datafix.fixes.References;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TransferableDataFieldRenameFix extends DataFix {
	public static final String COPPER_PIPE = SimpleCopperPipesConstants.id("copper_pipe").toString();
	public static final String COPPER_FITTING = SimpleCopperPipesConstants.id("fitting").toString();
	private final String blockEntity;

	public TransferableDataFieldRenameFix(String blockEntity, Schema outputSchema) {
		super(outputSchema, false);
		this.blockEntity = blockEntity;
	}

	@NotNull
	private static Dynamic<?> fixOccupants(@NotNull Dynamic<?> dynamic) {
		List<Dynamic<?>> oldDynamics = dynamic.get("saveableMoveableNbtList").orElseEmptyList().asStream().collect(Collectors.toCollection(ArrayList::new));
		dynamic = dynamic.remove("saveableMoveableNbtList");

		return dynamic.set("transferablePipeData", dynamic.createList(oldDynamics.stream()));
	}

	@Override
	protected TypeRewriteRule makeRule() {
		final Type<?> type = this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, this.blockEntity);
		final OpticFinder<?> opticFinder = DSL.namedChoice(this.blockEntity, type);

		return this.fixTypeEverywhereTyped(
			"saveable moveable nbt -> transferable data fix for " + this.blockEntity,
			this.getInputSchema().getType(References.BLOCK_ENTITY),
			this.getOutputSchema().getType(References.BLOCK_ENTITY),
			typed -> typed.updateTyped(opticFinder, this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, this.blockEntity), this::fix)
		);
	}

	@Contract("_ -> new")
	private @NotNull Typed<?> fix(@NotNull Typed<?> typed) {
		return typed.update(
			DSL.remainderFinder(),
			TransferableDataFieldRenameFix::fixOccupants
		);
	}
}
