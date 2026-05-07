package com.timyr_tm.rust_bound.world.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.electricity.ConnectionPointerInfo
import com.timyr_tm.rust_bound.world.electricity.WireType
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelReader

data class SpoolItemPointer(val name: String, val pos: BlockPos) {
    companion object {
        val CODEC: Codec<SpoolItemPointer> = RecordCodecBuilder.create {
            instance -> instance
                .group(
                    Codec.STRING.fieldOf("name").forGetter(SpoolItemPointer::name),
                    BlockPos.CODEC.fieldOf("pos").forGetter(SpoolItemPointer::pos)
                )
                .apply(instance, ::SpoolItemPointer)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, SpoolItemPointer> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SpoolItemPointer::name,
            BlockPos.STREAM_CODEC, SpoolItemPointer::pos,
            ::SpoolItemPointer
        )
    }

    fun getBlockEntity(level: LevelReader): ConnectableBlockEntity? = level.getBlockEntity(pos) as? ConnectableBlockEntity

    fun toConnectionPointer(wireType: ResourceKey<WireType>): ConnectionPointerInfo = ConnectionPointerInfo(name, pos, wireType)
}