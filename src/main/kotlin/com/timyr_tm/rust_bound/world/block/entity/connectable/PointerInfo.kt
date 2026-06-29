package com.timyr_tm.rust_bound.world.block.entity.connectable

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class PointerInfo(val name: String, val pos: BlockPos) {
    companion object {
        val CODEC: Codec<PointerInfo> = RecordCodecBuilder.create {
            instance -> instance
                .group(
                    Codec.STRING.fieldOf("name").forGetter(PointerInfo::name),
                    BlockPos.CODEC.fieldOf("pos").forGetter(PointerInfo::pos)
                )
                .apply(instance, ::PointerInfo)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, PointerInfo> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PointerInfo::name,
            BlockPos.STREAM_CODEC, PointerInfo::pos,
            ::PointerInfo
        )
    }

    constructor(pair: Pair<BlockPos, String>): this(pair.second, pair.first)
}