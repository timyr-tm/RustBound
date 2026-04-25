package com.timyr_tm.rust_bound.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.jspecify.annotations.Nullable;

public record ConnectionInfo(String name, BlockPos blockPos) {
    public @Nullable ConnectableBlockEntity getBlockEntity(LevelReader level) {
        return level.getBlockEntity(blockPos) instanceof ConnectableBlockEntity blockEntity
            ? blockEntity
            : null;
    }

    public @Nullable ConnectionPointInfo getConnectionPoint(LevelReader level) {
        if (level.getBlockEntity(blockPos) instanceof ConnectableBlockEntity blockEntity)
            return blockEntity.getConnectionPoints().stream()
                .filter(point -> point.name().equals(name))
                .findFirst()
                .orElse(null);
        return null;
    }
}
