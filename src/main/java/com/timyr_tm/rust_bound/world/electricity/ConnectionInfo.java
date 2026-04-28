package com.timyr_tm.rust_bound.world.electricity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.timyr_tm.rust_bound.core.registries.Registries;
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import org.jspecify.annotations.Nullable;

public record ConnectionInfo(String name, BlockPos pos, ResourceKey<WireType> wireType) {
    public static final Codec<ConnectionInfo> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ConnectionInfo::name),
            BlockPos.CODEC.fieldOf("pos").forGetter(ConnectionInfo::pos),
            ResourceKey.codec(Registries.WIRE_TYPE).fieldOf("wire_type").forGetter(ConnectionInfo::wireType)
        ).apply(instance, ConnectionInfo::new)
    );

    public static final StreamCodec<ByteBuf, ConnectionInfo> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ConnectionInfo::name,
        BlockPos.STREAM_CODEC, ConnectionInfo::pos,
        ResourceKey.streamCodec(Registries.WIRE_TYPE), ConnectionInfo::wireType,
        ConnectionInfo::new
    );

    public @Nullable ConnectableBlockEntity getBlockEntity(LevelReader level) {
        return level.getBlockEntity(pos) instanceof ConnectableBlockEntity blockEntity
            ? blockEntity
            : null;
    }

    public @Nullable ConnectionPointInfo getConnectionPoint(LevelReader level) {
        if (level.getBlockEntity(pos) instanceof ConnectableBlockEntity blockEntity)
            return blockEntity.getConnectionPoints().getOrDefault(name, null);
        return null;
    }
}
