package net.lunade.copper.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperFittingBlock extends CopperFittingBlock implements WeatheringCopper {
	public static final MapCodec<WeatheringCopperFittingBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		WeatherState.CODEC.fieldOf("weather_state").forGetter((copperPipe -> copperPipe.weatherState)),
		propertiesCodec()
	).apply(instance, WeatheringCopperFittingBlock::new));
	private final WeatherState weatherState;

	public WeatheringCopperFittingBlock(WeatherState weatherState, Properties settings) {
		super(settings);
		this.weatherState = weatherState;
	}

	@Override
	public int getCooldown() {
		return 1;
	}

	@Override
	public void randomTick(@NotNull BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.randomTick(state, level, pos, random);
		this.changeOverTime(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent() || super.isRandomlyTicking(state);
	}

	@Override
	public @NotNull WeatherState getAge() {
		return this.weatherState;
	}

	@Override
	protected @NotNull MapCodec<? extends WeatheringCopperFittingBlock> codec() {
		return CODEC;
	}
}
