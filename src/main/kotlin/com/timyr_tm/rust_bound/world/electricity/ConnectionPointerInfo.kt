package com.timyr_tm.rust_bound.world.electricity

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.timyr_tm.rust_bound.core.registries.Registries
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelReader

data class ConnectionPointerInfo(val name: String, val pos: BlockPos) {
    companion object {
        val CODEC: Codec<ConnectionPointerInfo> = RecordCodecBuilder.create {
            instance -> instance
                .group(
                    Codec.STRING.fieldOf("name").forGetter(ConnectionPointerInfo::name),
                    BlockPos.CODEC.fieldOf("pos").forGetter(ConnectionPointerInfo::pos)
                )
                .apply(instance, ::ConnectionPointerInfo)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, ConnectionPointerInfo> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ConnectionPointerInfo::name,
            BlockPos.STREAM_CODEC, ConnectionPointerInfo::pos,
            ::ConnectionPointerInfo
        )
    }

    fun getBlockEntity(level: LevelReader): ConnectableBlockEntity? = level.getBlockEntity(pos) as? ConnectableBlockEntity

    fun getConnectionPoint(level: LevelReader): ConnectionPointInfo? = getBlockEntity(level)?.connections[name]
}