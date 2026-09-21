/*
 * Copyright 2026 Lunade Music/AViewFromTheTop
 * This file is part of Simple Copper Pipes.
 *
 * This program is free software; you can modify it under
 * the terms of version 1 of the FrozenBlock Modding Oasis License
 * as published by FrozenBlock Modding Oasis.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * FrozenBlock Modding Oasis License for more details.
 *
 * You should have received a copy of the FrozenBlock Modding Oasis License
 * along with this program; if not, see <https://github.com/FrozenBlock/Licenses>.
 */

package net.lunade.copper.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperPipeBlock extends CopperPipeBlock implements WeatheringCopper {
	public static final MapCodec<WeatheringCopperPipeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		WeatherState.CODEC.fieldOf("weather_state").forGetter(copperPipe -> copperPipe.weatherState),
		propertiesCodec(),
		Codec.INT.fieldOf("dispense_shot_power").forGetter(copperPipe -> copperPipe.dispenseShotPower)
	).apply(instance, WeatheringCopperPipeBlock::new));
	public final int dispenseShotPower;
	private final WeatherState weatherState;

	public WeatheringCopperPipeBlock(WeatherState weatherState, Properties properties, int dispenseShotPower) {
		super(properties, dispenseShotPower);
		this.weatherState = weatherState;
		this.dispenseShotPower = dispenseShotPower;
	}

	@Override
	public int getCooldown() {
		return 2;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.randomTick(state, level, pos, random);
		this.changeOverTime(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent() || super.isRandomlyTicking(state);
	}

	@Override
	public WeatherState getAge() {
		return this.weatherState;
	}

	@Override
	protected MapCodec<? extends WeatheringCopperPipeBlock> codec() {
		return CODEC;
	}
}
