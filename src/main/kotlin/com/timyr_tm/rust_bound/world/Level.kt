package com.timyr_tm.rust_bound.world

import com.timyr_tm.rust_bound.world.block.entity.connectable.ConnectableBlockEntity
import com.timyr_tm.rust_bound.world.block.entity.connectable.PointInfo
import com.timyr_tm.rust_bound.world.block.entity.connectable.PointerInfo
import net.minecraft.world.level.BlockGetter

object Level {
    fun BlockGetter.getBlockEntity(pointer: PointerInfo): ConnectableBlockEntity? = this.getBlockEntity(pointer.pos) as? ConnectableBlockEntity

    fun BlockGetter.getPoint(pointer: PointerInfo): PointInfo? = this.getBlockEntity(pointer)?.connections?.points
        ?.firstOrNull { it.name == pointer.name }
}