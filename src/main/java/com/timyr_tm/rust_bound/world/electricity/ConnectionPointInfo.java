package com.timyr_tm.rust_bound.world.electricity;

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ConnectionPointInfo(
    Vec3 point, VoxelShape shape,
    BlockPos pos, boolean enabled
) {
    public @Nullable ConnectableBlockEntity getBlockEntity(LevelReader level) {
        return level.getBlockEntity(pos) instanceof ConnectableBlockEntity blockEntity
            ? blockEntity
            : null;
    }

    public @NonNull BlockState getBlockState(LevelReader level) {
        return level.getBlockState(pos);
    }
}
