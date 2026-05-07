package com.timyr_tm.rust_bound.world.electricity

import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape

class ConnectionPointInfo(val point: Vec3, val shape: VoxelShape, val pos: BlockPos): Iterable<ConnectionPointerInfo> {
    private val connections: MutableSet<ConnectionPointerInfo> = HashSet()

    fun getBlockEntity(level: LevelReader): ConnectableBlockEntity? = level.getBlockEntity(pos) as? ConnectableBlockEntity

    fun getBlockState(level: LevelReader): BlockState = level.getBlockState(pos)

    override fun iterator(): Iterator<ConnectionPointerInfo> = this.connections.iterator()

    operator fun plusAssign(connections: Iterable<ConnectionPointerInfo>) {
        if (connections.any {connection -> connection.pos == this.pos})
            throw RuntimeException()
        this.connections.addAll(connections)
    }

    operator fun plusAssign(connection: ConnectionPointerInfo) {
        if (connection.pos == this.pos)
            throw RuntimeException()
        this.connections.add(connection)
    }

    operator fun minusAssign(connections: Set<ConnectionPointerInfo>) {
        this.connections.removeAll(connections)
    }

    operator fun minusAssign(connection: ConnectionPointerInfo) {
        this.connections.remove(connection)
    }

    operator fun contains(connection: ConnectionPointerInfo) = connection in this.connections
}