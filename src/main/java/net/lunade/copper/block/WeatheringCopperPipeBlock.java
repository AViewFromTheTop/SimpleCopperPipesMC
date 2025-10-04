package net.lunade.copper.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperPipeBlock extends CopperPipeBlock implements WeatheringCopper {
	public static final MapCodec<WeatheringCopperPipeBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		WeatherState.CODEC.fieldOf("weather_state").forGetter((copperPipe -> copperPipe.weatherState)),
		propertiesCodec(),
		Codec.INT.fieldOf("dispense_shot_power").forGetter((copperPipe) -> copperPipe.dispenseShotPower)
	).apply(instance, WeatheringCopperPipeBlock::new));
	public final int dispenseShotPower;
	private final WeatherState weatherState;

	public WeatheringCopperPipeBlock(WeatherState weatherState, Properties settings, int dispenseShotPower) {
		super(settings, dispenseShotPower);
		this.weatherState = weatherState;
		this.dispenseShotPower = dispenseShotPower;
	}

	@Override
	public int getCooldown() {
		return 2;
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
	protected @NotNull MapCodec<? extends WeatheringCopperPipeBlock> codec() {
		return CODEC;
	}
}
