package com.timyr_tm.rust_bound.world.electricity

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape

data class ConnectionPointInfo(
    val point: Vec3,
    val shape: VoxelShape,
    val pos: BlockPos,
    val enabled: Boolean
)