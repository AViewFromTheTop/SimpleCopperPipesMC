package net.lunade.copper.game_event;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class ExtraPipeData {

    private static final Logger LOGGER = LogUtils.getLogger();
    public BlockPos listenerPos;

    public static final Codec<ExtraPipeData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockPos.CODEC.fieldOf("listenerPos").forGetter(ExtraPipeData::getListenerPos)
    ).apply(instance, ExtraPipeData::new));

    public ExtraPipeData(BlockPos listenerPos) {
        this.listenerPos = listenerPos;
    }

    public BlockPos getListenerPos() {
        return this.listenerPos;
    }

    public static ExtraPipeData readNbt(NbtCompound nbt) {
        Optional<ExtraPipeData> extraPipeData = Optional.empty();
        if (nbt.contains("savedExtraPipeData", 10)) {
            extraPipeData = ExtraPipeData.CODEC
                    .parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("savedExtraPipeData")))
                    .resultOrPartial(LOGGER::error);
        }
        return extraPipeData.orElse(null);
    }

    public static void writeNbt(NbtCompound nbt, @Nullable ExtraPipeData extraPipeData) {
        if (extraPipeData != null) {
            ExtraPipeData.CODEC
                    .encodeStart(NbtOps.INSTANCE, extraPipeData)
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(extraPipeDataNbt -> nbt.put("savedExtraPipeData", extraPipeDataNbt));
        } else {
            if (nbt.contains("savedExtraPipeData", 10)) {
                nbt.remove("savedExtraPipeData");
            }
        }
    }

}
